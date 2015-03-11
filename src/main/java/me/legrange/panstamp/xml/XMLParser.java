package me.legrange.panstamp.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import me.legrange.panstamp.definition.Direction;
import me.legrange.panstamp.definition.Position;
import me.legrange.panstamp.definition.Size;
import me.legrange.panstamp.definition.Type;
import me.legrange.panstamp.definition.Unit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class parses XML device definitions into DeviceDefinitons objects. 
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
 final class XMLParser {

     /** Parse all XML definitions in the supplied library and return a list definitions. 
      * 
      * @param lib The library from which to read the device definitions. 
      * @return The list of parsed definitions.
      * @throws ParseException Thrown if there is a problem parsing the defintions. 
      */
    public static List<XMLDeviceDefinition> parse(XMLDeviceLibrary lib) throws ParseException {
        XMLParser parser = new XMLParser(lib);
        return parser.parseDevices();
    }

    /**
     * create a new parser
     */
    private XMLParser(XMLDeviceLibrary lib) {
        this.lib = lib;
    }

    /**
     * parse the devices.xml file
     */
    private List<XMLDeviceDefinition> parseDevices() throws ParseException {
        String fileName = "devices.xml";
        List<XMLDeviceDefinition> devices = new LinkedList<>();
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
    private List<XMLDeviceDefinition> parseDeveloper(Element node) throws ParseException {
        List<XMLDeviceDefinition> devices = new LinkedList<>();
        int id = requireIntAttr(node, "id");
        String name = requireAttr(node, "name");
        XMLDeveloperDefinition dev = new XMLDeveloperDefinition(id, name);
        for (Element n : iterable(node.getChildNodes())) {
            if (n.getNodeName().equals("dev")) {
                XMLDeviceDefinition device = parseDevice(dev, n);
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
    private XMLDeviceDefinition parseDevice(XMLDeveloperDefinition devel, Element node) throws ParseException {
        XMLDeviceDefinition dev = null;
        int id = requireIntAttr(node, "id");
        String name = requireAttr(node, "name");
        String label = requireAttr(node, "label");
        String fileName = devel.getName() + "/" + name + ".xml";
        InputStream in = makeStream(fileName);
        if (in !=  null) {
            dev = new XMLDeviceDefinition(devel, id, name, label);
            parseDeviceXML(dev, fileName);
        } else {
            log.warning(String.format("File '%s' for device id %d does not exist, skipping.", fileName, id));
        }
        return dev;
    }

    /**
     * parse the device XML file
     */
    private void parseDeviceXML(XMLDeviceDefinition dev, String fileName) throws ParseException {
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
    private void parseConfig(XMLDeviceDefinition dev, Element config) throws ParseException {
        for (Element node : iterable(config.getChildNodes())) {
            switch (node.getNodeName()) {
                case "reg":
                    dev.addRegister(parseConfigReg(dev, node));
                    break;
                default:
                    throw new ParseException(String.format(String.format("Unexpected element '%s'", node.getNodeName())));
            }
        }
    }

    /**
     * parse a config/reg element
     */
    private XMLRegisterDefinition parseConfigReg(XMLDeviceDefinition dev, Element reg) throws ParseException {
        int id = requireIntAttr(reg, "id");
        String name = requireAttr(reg, "name");
        XMLRegisterDefinition register = new XMLRegisterDefinition(id, name);
        for (Element node : iterable(reg.getChildNodes())) {
            switch (node.getNodeName()) {
                case "param":
                    parseParam(register, node);
                    break;
                default:
                    throw new ParseException(String.format("Unexpected element '%s'", node.getNodeName()));
            }
        }
        return register;
    }

    /**
     * parse a register parameter
     */
    private void parseParam(XMLRegisterDefinition register, Element param) throws ParseException {
        String name = requireAttr(param, "name");
        Type type = Type.forTag(requireAttr(param, "type"));
        if (type == null) {
            throw new ParseException(String.format("Unexpected param type '%s'", requireAttr(param, "type")));
        }
        XMLParameterDefinition par = new XMLParameterDefinition(name, type);
        for (Element node : iterable(param.getChildNodes())) {
            switch (node.getNodeName()) {
                case "position":
                    parsePosition(par, node);
                    break;
                case "size":
                    parseSize(par, node);
                    break;
                case "default":
                    par.setDefault(requireText(node));
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
    private void parsePosition(XMLParameterDefinition par, Element pos) throws ParseException {
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
    private void parseSize(XMLParameterDefinition par, Element pos) throws ParseException {
        String text = requireText(pos);
        try {
            if (text.matches("[0-9]+\\.[0-9]+")) {
                String parts[] = text.split("\\.");
                par.setSize(new Size(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            } else {
                par.setSize(new Size(Integer.parseInt(text),0));
            }

        } catch (NumberFormatException e) {
            throw new ParseException(String.format("Value for '%s' is not an integer", pos.getNodeName()), e);
        }
    }

    /**
     * parse the regular section
     */
    private void parseRegular(XMLDeviceDefinition dev, Element regular) throws ParseException {
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
    private XMLRegisterDefinition parseRegularReg(XMLDeviceDefinition dev, Element reg) throws ParseException {
        int id = requireIntAttr(reg, "id");
        String name = requireAttr(reg, "name");
        XMLRegisterDefinition register = new XMLRegisterDefinition(id, name);
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
    private XMLEndpointDefinition parseEndpoint(XMLRegisterDefinition register, Element endp) throws ParseException {
        String name = requireAttr(endp, "name");
        Type type = Type.forTag(requireAttr(endp, "type"));
        if (type == null) {
            throw new ParseException(String.format("Unexpected endpoint type '%s'", requireAttr(endp, "type")));
        }
        Direction dir = Direction.forTag(requireAttr(endp, "dir"));
        if (dir == null) {
            throw new ParseException(String.format("Unexpected endpoint direction '%s'", requireAttr(endp, "dir")));
        }
        XMLEndpointDefinition endpoint = new XMLEndpointDefinition(register, name, dir, type);
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
    private void parsePosition(XMLEndpointDefinition endpoint, Element pos) throws ParseException {
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
    private void parseSize(XMLEndpointDefinition endpoint, Element pos) throws ParseException {
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
    private void parseUnits(XMLEndpointDefinition endpoint, Element units) throws ParseException {
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
    private void parseUnit(XMLEndpointDefinition endpoint, Element u) throws ParseException {
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

    private final XMLDeviceLibrary lib;
    private final static Logger log = Logger.getLogger(XMLParser.class.getName());
}
