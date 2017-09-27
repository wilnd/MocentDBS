package decode;

public class JpegParam {
	public static final byte JPEG_ENVRECORD  = 1;
	public static final byte JPEG_APPRECORD  = 2;
	public static final byte JPEG_MOCRECORD  = 9;
	
	public static final byte JPEG_SUBJECT = 12;
	public static final byte JPEG_CAPTION = 120;
	
	public static final byte JPEG_QUICKMSGLINE1  = 17;
	public static final byte JPEG_QUICKMSGLINE2  = 18;
	public static final byte JPEG_QUICKMSGLINE3  = 19;
	public static final byte JPEG_QUICKMSGLINE4  = 20;
	public static final byte JPEG_QUICKMSGLINE5  = 21;
	
	/***********************************  JPEG Markers *************************************/

	public static final byte JPG_SOF0[]  = {(byte) 0xFF,(byte) 0xC0}; // Start of frame
	public static final byte JPG_DHT[]   = {(byte) 0xFF,(byte) 0xC4}; // Huffman table 

	public static final byte JPG_SOI[]   = {(byte) 0xFF,(byte) 0xD8}; // Start of image
	public static final byte JPG_EOI[]   = {(byte) 0xFF,(byte) 0xD9}; // End of image
	public static final byte JPG_SOS[]   = {(byte) 0xFF,(byte) 0xDA}; // Start of Scan
	public static final byte JPG_DQT[]   = {(byte) 0xFF,(byte) 0xDB}; // Quantization Table

	public static final byte JPG_APP0[]  = {(byte) 0xFF,(byte) 0xE0}; // APP0, JFIF
	public static final byte JPG_APP1[]  = {(byte) 0xFF,(byte) 0xE1}; // APP1, Exif
	public static final byte JPG_APP2[]  = {(byte) 0xFF,(byte) 0xE2}; // APP2
	public static final byte JPG_APP14[] = {(byte) 0xFF,(byte) 0xED}; // APP14 marker, Photoshop stores its information in this seg
	public static final byte JPG_CMT[]   = {(byte) 0xFF,(byte) 0xFE}; // Comment
	public static final byte HEADER_8BIM_TYPE[]   = {(byte) 0x4,(byte) 0x4}; 
	public static final String PHOTOSHOP_8BIM = "8BIM";
}
