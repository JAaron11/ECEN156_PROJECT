/**
 * A basic class implemented to write to both the output.txt and to the console as well.
 */

package com.vgb;

import java.io.PrintStream;
import java.io.OutputStream;

public class TeePrintStream extends PrintStream{
	private final PrintStream second;
	
	public TeePrintStream(OutputStream main, PrintStream second) {
		super(main, true);
		this.second = second;
	}
	
	public void write(byte[] buf, int off, int len) {
		// Write to both steams
		super.write(buf, off, len);
		second.write(buf, off, len);
	}
	
	public void flush() {
		super.flush();
		second.flush();
	}

}
