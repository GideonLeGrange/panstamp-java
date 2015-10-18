package me.legrange.panstamp;

import me.legrange.panstamp.definition.ParameterDefinition;

/**
 * An parameter that supports the "btr" type from the XML definitions and maps
 * these to byte arrays. 
 * 
 * @since 2.1
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class ByteArrayParameter extends AbstractParameter<byte[]> {

    ByteArrayParameter(Register reg, ParameterDefinition epDef) {
        super(reg, epDef);
        
    }

    @Override
    public Type getType() {
        return Type.BYTE_ARRAY;
    }
    
   
    @Override
    public byte[] getValue() throws NetworkException {
        byte bytes[]  = reg.getValue();
        byte keep[] = new byte[par.getSize().getBytes()];
        System.arraycopy(bytes, par.getPosition().getBytePos(), keep, 0, par.getSize().getBytes());
        return keep;
    }
  
    @Override
    public void setValue(byte[] value) throws NetworkException {
        int len = par.getSize().getBytes();
        byte bytes[] = new byte[len];
        if (value.length < len) {
            len = value.length;
        } 
        System.arraycopy(value, 0, bytes, par.getPosition().getBytePos(), len);
        reg.setValue(bytes);
    }

    @Override
    public byte[] getDefault() {
        String def = par.getDefault().trim().toLowerCase();
        if (def.startsWith("0x")) { 
            def = def.substring(2);
        }
        int len = def.length();
        if ((len % 2) != 0) {
            def = "0" + def;
            len ++;
        }
        len = len / 2;
        while (len < par.getSize().getBytes()) {
            def = "00" + def;
            len ++;
        }
        byte bytes[] = new byte[len];
        for (int i = 0; i < len; ++i) {
            int val = Integer.parseInt("" + def.charAt(i*2) + def.charAt(i*2 + 1), 16);
            if (val > 127) {
                val = val - 256;
            }
            bytes[i] = (byte)val;
        }
        return bytes;
    }
    
    
    
}
