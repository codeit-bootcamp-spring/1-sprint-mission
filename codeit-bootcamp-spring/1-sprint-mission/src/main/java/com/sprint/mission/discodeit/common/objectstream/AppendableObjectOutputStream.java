package com.sprint.mission.discodeit.common.objectstream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class AppendableObjectOutputStream extends ObjectOutputStream {


    private boolean append;
    private boolean initialized;
    private DataOutputStream dout;

    protected AppendableObjectOutputStream(boolean append) throws IOException, SecurityException {
        super();
        this.append = append;
        this.initialized = true;
    }

    public AppendableObjectOutputStream(OutputStream out, boolean append) throws IOException {
        super(out);
        this.append = append;
        this.initialized = true;
        this.dout = new DataOutputStream(out);
        this.writeStreamHeader();
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        if (!this.initialized || this.append) return;
        if (dout != null) {
            dout.writeShort(STREAM_MAGIC);
            dout.writeShort(STREAM_VERSION);
        }
    }
}
/**
 *  https://stackoverflow.com/questions/1194656/appending-to-an-objectoutputstream
 */
