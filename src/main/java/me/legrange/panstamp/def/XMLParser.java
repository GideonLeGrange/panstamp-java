package me.legrange.panstamp.def;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Parse XML Devices XML file
 *
 * @author gideon
 */
public class XMLParser {

    public static List<Device> parse(DeviceLibrary lib) throws ParseException {
        XMLParser parser = new XMLParser(lib);
        return parser.parseDevices();
    }

    /**
     * create a new parser
     */
    private XMLParser(DeviceLibrary lib) {
        this.lib = lib;
    }

    /**
     * parse the devices.xml file
     */
    private List<Device> parseDevices() throws ParseException {
        String fileName = "devices.xml";
        List<Device> devices = new LinkedList<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(makeStream(fileName));

            Element root = doc.getDocumentElement();
            if (!root.getNodeName().equals("devices")) {
                throw new ParseException(String.format("Excpected 'devices' element, but found '%s'", root.getNodeName()));
            }
            for (Element node : iterable(root.getElementsByTagName("developer"))) {
                devices.addAll(parseDeveloper(node));
            }
        } catch (ParserConfigurationException | SAXException ex) {
            throw new ParseException(String.format("XML error parsing '%s': %s", fileName, ex.getMessage()), ex);
        } catch (IOException ex) {
            throw new ParseException(String.format("IO error parsing '%s': %s", fileName, ex.getMessage()), ex);
        }
        return devices;
    }

    /**
     * parse the devices for one developer
     */
    private List<Device> parseDeveloper(Element node) throws ParseException {
        List<Device> devices = new LinkedList<>();
        int id = requireIntAttr(node, "id");
        String name = requireAttr(node, "name");
        Developer dev = new Developer(id, name);
        for (Element n : iterable(node.getChildNodes())) {
            if (n.getNodeName().equals("dev")) {
                Device device = parseDevice(dev, n);
                if (device != null) {
                    devices.add(device);
                }
            }
        }
        return devices;
    }

    /**
     * parse a device configuration
     */
    private Device parseDevice(Developer devel, Element node) throws ParseException {
        Device dev = null;
        int id = requireIntAttr(node, "id");
        String name = requireAttr(node, "name");
        String label = requireAttr(node, "label");
        String fileName = devel.getName() + "/" + name + ".xml";
        InputStream in = makeStream(fileName);
        if (in !=  null) {
            dev = new Device(devel, id, name, label);
            parseDeviceXML(dev, fileName);
        } else {
            log.warning(String.format("File '%s' for device id %d does not exist, skipping.", fileName, id));
        }
        return dev;
    }

    /**
     * parse the device XML file
     */
    private void parseDeviceXML(Device dev, String fileName) throws ParseException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(makeStream(fileName));
            Element root = doc.getDocumentElement();
            if (!root.getNodeName().equals("device")) {
                throw new ParseException(String.format("Excpected 'device' element, but found '%s'", root.getNodeName()));
            }

            for (Element node : iterable(root.getChildNodes())) {
                switch (node.getNodeName()) {
                    case "developer":
                        break;
                    case "product":
                        dev.setProduct(node.getTextContent());
                        break;
                    case "pwrdownmode":
                        dev.setPowerDownMode(Boolean.valueOf(node.getTextContent()));
                        break;
                    case "regular":
                        parseRegular(dev, node);
                        break;
                    case "config":
                        parseConfig(dev, node);
                        break;
                    default:
                        throw new ParseException(String.format("Unexpected element '%s' in device file '%s'", node.getNodeName(), fileName));
                }
            }
        } catch (ParserConfigurationException | SAXException ex) {
            throw new ParseException(String.format("XML error parsing '%s': %s", fileName, ex.getMessage()), ex);
        } catch (IOException ex) {
            throw new ParseException(String.format("IO error parsing '%s': %s", fileName, ex.getMessage()), ex);
        }
    }

    /**
     * parse the config section
     */
    private void parseConfig(Device dev, Element config) throws ParseException {
        for (Element node : iterable(config.getChildNodes())) {
            switch (node.getNodeName()) {
                case "reg":
                    parseConfigReg(dev, node);
                    break;
                default:
                    throw new ParseException(String.format(String.format("Unexpected element '%s'", node.getNodeName())));
            }
        }
    }

    /**
     * parse a config/reg element
     */
    private void parseConfigReg(Device dev, Element reg) throws ParseException {
        int id = requireIntAttr(reg, "id");
        String name = requireAttr(reg, "name");
        RegisterDef register = new RegisterDef(id, name);
        for (Element node : iterable(reg.getChildNodes())) {
            switch (node.getNodeName()) {
                case "param":
                    parseParam(register, node);
                    break;
                default:
                    throw new ParseException(String.format("Unexpected element '%s'", node.getNodeName()));
            }
        }
    }

    /**
     * parse a register parameter
     */
    private void parseParam(RegisterDef register, Element param) throws ParseException {
        String name = requireAttr(param, "name");
        Type type = Type.forTag(requireAttr(param, "type"));
        if (type == null) {
            throw new ParseException(String.format("Unexpected param type '%s'", requireAttr(param, "type")));
        }
        Param par = new Param(name, type);
        for (Element node : iterable(param.getChildNodes())) {
            switch (node.getNodeName()) {
                case "position":
                    parsePosition(par, node);
                    break;
                case "size":
                    parseSize(par, node);
                    break;
                case "default":
                    par.setDef(requireText(node));
                    break;
                case "verif":
                    par.setVerif(requireText(node));
                    break;
                default:
                    throw new ParseException(String.format("Unexpected element '%s' in element '%s'", node.getNodeName(), param.getNodeName()));
            }
        }
        register.addParameter(par);
    }

    /**
     * parse param position
     */
    private void parsePosition(Param par, Element pos) throws ParseException {
        String text = requireText(pos);
        try {
            if (text.matches("[0-9]+\\.[0-9]+")) {
                String parts[] = text.split("\\.");
                par.setPosition(new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            } else {
                par.setPosition(new Position(Integer.parseInt(text)));
            }

        } catch (NumberFormatException e) {
            throw new ParseException(String.format("Value for '%s' is not an integer", pos.getNodeName()), e);
        }
    }

    /**
     * parse param size
     */
    private void parseSize(Param par, Element pos) throws ParseException {
        String text = requireText(pos);
        try {
            if (text.matches("[0-9]+\\.[0-9]+")) {
                String parts[] = text.split("\\.");
                par.setSize(new Size(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            } else {
                par.setPosition(new Position(Integer.parseInt(text)));
            }

        } catch (NumberFormatException e) {
            throw new ParseException(String.format("Value for '%s' is not an integer", pos.getNodeName()), e);
        }
    }

    /**
     * parse the regular section
     */
    private void parseRegular(Device dev, Element regular) throws ParseException {
        for (Element node : iterable(regular.getChildNodes())) {
            switch (node.getNodeName()) {
                case "reg":
                    dev.addRegister(parseRegularReg(dev, node));
                    break;
                default:
                    throw new ParseException(String.format("Unexpected element '%s'", node.getNodeName()));
            }
        }

    }

    /**
     * parse a reg element
     */
    private RegisterDef parseRegularReg(Device dev, Element reg) throws ParseException {
        int id = requireIntAttr(reg, "id");
        String name = requireAttr(reg, "name");
        RegisterDef register = new RegisterDef(id, name);
        for (Element node : iterable(reg.getChildNodes())) {
            switch (node.getNodeName()) {
                case "endpoint":
                    register.addEndpoint(parseEndpoint(register, node));
                    break;
                default:
                    throw new ParseException(String.format("Unexpected element '%s'", node.getNodeName()));
            }
        }
        return register;
    }

    /**
     * parse a register end point
     */
    private EndpointDef parseEndpoint(RegisterDef register, Element endp) throws ParseException {
        String name = requireAttr(endp, "name");
        Type type = Type.forTag(requireAttr(endp, "type"));
        if (type == null) {
            throw new ParseException(String.format("Unexpected endpoint type '%s'", requireAttr(endp, "type")));
        }
        Direction dir = Direction.forTag(requireAttr(endp, "dir"));
        if (dir == null) {
            throw new ParseException(String.format("Unexpected endpoint direction '%s'", requireAttr(endp, "dir")));
        }
        EndpointDef endpoint = new EndpointDef(register, name, dir, type);
        for (Element node : iterable(endp.getChildNodes())) {
            switch (node.getNodeName()) {
                case "position":
                    parsePosition(endpoint, node);
                    break;
                case "size":
                    parseSize(endpoint, node);
                    break;
                case "units":
                    parseUnits(endpoint, node);
                    break;
                default:
                    throw new ParseException(String.format("Unexpected element '%s' in element '%s'", node.getNodeName(), endp.getNodeName()));
            }
        }
        return endpoint;
    }

    /**
     * parse endpoint position
     */
    private void parsePosition(EndpointDef endpoint, Element pos) throws ParseException {
        String text = requireText(pos);
        try {
            if (text.matches("[0-9]+\\.[0-9]+")) {
                String parts[] = text.split("\\.");
                endpoint.setPosition(new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            } else {
                endpoint.setPosition(new Position(Integer.parseInt(text)));
            }

        } catch (NumberFormatException e) {
            throw new ParseException(String.format("Value for '%s' is not an integer", pos.getNodeName()), e);
        }
    }

    /**
     * parse endpoint size
     */
    private void parseSize(EndpointDef endpoint, Element pos) throws ParseException {
        String text = requireText(pos);
        try {
            if (text.matches("[0-9]+\\.[0-9]+")) {
                String parts[] = text.split("\\.");
                endpoint.setSize(new Size(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            } else {
                endpoint.setSize(new Size(Integer.parseInt(text), 0));
            }

        } catch (NumberFormatException e) {
            throw new ParseException(String.format("Value for '%s' is not an integer", pos.getNodeName()), e);
        }
    }

    /**
     * parse endpoint units
     */
    private void parseUnits(EndpointDef endpoint, Element units) throws ParseException {
        for (Element node : iterable(units.getChildNodes())) {
            switch (node.getNodeName()) {
                case "unit":
                    parseUnit(endpoint, node);
                    break;
                default:
                    throw new ParseException(String.format("Unexpected element '%s'", node.getNodeName()));
            }
        }

    }

    /**
     * parse an endpoint unit
     */
    private void parseUnit(EndpointDef endpoint, Element u) throws ParseException {
        String name = "";
        if (u.hasAttribute("name")) {
            name = u.getAttribute("name");
        }
        double factor = requireDoubleAttr(u, "factor");
        double offset = requireDoubleAttr(u, "offset");
        endpoint.addUnit(new Unit(name, factor, offset));
    }

    /**
     * make sure the node has non-blank text
     */
    private String requireText(Element node) throws ParseException {
        String text = node.getTextContent().trim();
        if ((text == null) || text.equals("")) {
            throw new ParseException(String.format("Text for node '%s' is empty", node.getNodeName()));
        }
        return text;
    }

    private int requireIntText(Element node) throws ParseException {
        try {
            return Integer.parseInt(requireText(node));
        } catch (NumberFormatException e) {
            throw new ParseException(String.format("Value for '%s' is not an integer", node.getNodeName()), e);
        }

    }

    /**
     * make sure the node has the named non-blank attribute
     */
    private String requireAttr(Element node, String name) throws ParseException {
        if (!node.hasAttribute(name)) {
            throw new ParseException(String.format("Attribute '%s' is not defined on node '%s'", name, node.getNodeName()));
        }
        String text = node.getAttribute(name);
        if ((text == null) || text.equals("")) {
            throw new ParseException(String.format("Attribute '%s' is not defined", name));
        }
        return text;
    }

    private int requireIntAttr(Element node, String name) throws ParseException {
        try {
            return Integer.parseInt(requireAttr(node, name));
        } catch (NumberFormatException e) {
            throw new ParseException(String.format("Attribute '%s' is not an integer", name), e);
        }
    }

    private double requireDoubleAttr(Element node, String name) throws ParseException {
        try {
            return Double.parseDouble(requireAttr(node, name));
        } catch (NumberFormatException e) {
            throw new ParseException(String.format("Attribute '%s' is not a number", name), e);
        }
    }

    private InputStream makeStream(String fileName) {
        return lib.getStream(fileName); 
    }
    
    private static Iterable<Element> iterable(final NodeList nl) {
        List<Element> els = new LinkedList<>();
        for (int i = 0; i < nl.getLength(); ++i) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                els.add((Element) n);
            }
        }
        return els;
    }

    private final DeviceLibrary lib;
    private final static Logger log = Logger.getLogger(XMLParser.class.getName());
}
