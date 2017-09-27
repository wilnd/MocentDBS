package decode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class JpegDecoder {
	
	private byte[] mIptc;
	private int mIptcLength;
	private List<TagInfo> mTagList = new ArrayList<TagInfo>();
	
	/**
	 * 读取标题
	 **/
	public String getCaption() {
		return getStrTag(JpegParam.JPEG_APPRECORD, JpegParam.JPEG_CAPTION);
	}
	
	/**
	 * 读取主题
	 **/
	public String getSubject() {
		return getStrTag(JpegParam.JPEG_APPRECORD, JpegParam.JPEG_SUBJECT);
	}
	
	/**
	 * 读取消息内容
	 **/
	public String getQuckMsgLine(int idxLine) {
		if (0 <= idxLine && idxLine < 5) {
			return getStrTag(JpegParam.JPEG_MOCRECORD, (byte)(JpegParam.JPEG_QUICKMSGLINE1 + idxLine));
		} else {
			return null;
		}
	}
	
	public boolean loadJpegFile(String fileName) {
		File file;
		FileInputStream fin;
		
		file = new File(fileName);
		if (!file.exists()) {
			return false;
		}
		
		try {
			fin = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} 
		return loadJpegFile(fin);
	}
	
	public boolean loadJpegFile(InputStream fin) {
		byte[] soi = new byte[4];
		byte[] seg = new byte[4];
		int pos = 0;
		int byteRead = 0;
		int seg_len = 0;
		
		try {   
	        int length = fin.available();   
	        if (length < 24) {
	        	fin.close();
	        	return false;
	        }
	        
	        byteRead = fin.read(soi, 0, 2);   
	        if (byteRead != 2) {
	        	fin.close();
	        	return false;
	        }
	        pos += byteRead;
	        
	        if (!compareByteArray(soi, JpegParam.JPG_SOI)) {
	        	fin.close();
	        	return false;
	        }
	       
	        boolean bFoundAppMarker = false;
	        while (!bFoundAppMarker && pos < length) {
	        	byteRead = fin.read(seg, 0, 4);
	        	if (byteRead != 4) {
		        	fin.close();
		        	return false;
		        }
	        	pos += byteRead;
	        	if (seg[0] != (byte)0xFF) {
	        		fin.close();
		        	return false;
	        	}
	        	seg_len = ((int)(seg[2] & 0xFF)<<8) + (seg[3] & 0xFF);
	        	
	        	if (compareByteArray(seg, JpegParam.JPG_SOS)) {
	        		seg_len = 0;
	    			break;
	        	}
	        	
	        	if (compareByteArray(seg, JpegParam.JPG_APP14)) {
	        		bFoundAppMarker = true;
	    			break;
	        	}
	        	
	        	pos += fin.skip(seg_len - 2);
	        }
	        
	        if (bFoundAppMarker) {
	        	int appMarkerLen = seg_len - 2;
	        	byte[] appMarker = new byte[appMarkerLen];
	        	byteRead = fin.read(appMarker, 0, appMarkerLen);
	        	if (byteRead != appMarkerLen) {
		        	fin.close();
		        	return false;
		        }
	        	
	        	byte[] header8BIM = get8BIMHeader(appMarker, JpegParam.HEADER_8BIM_TYPE, appMarkerLen);
	        	if (header8BIM != null) {
	        		mIptcLength = ((int)(header8BIM[0] & 0xFF)<<24) + ((int)(header8BIM[1] & 0xFF)<<16) + ((int)(header8BIM[2] & 0xFF)<<8) + (int)(header8BIM[3] & 0xFF);
	        		mIptc = sub(header8BIM, 4);
	        		
	        		if (setTagList() == 0) {
	        			mIptc = null;
	        			mIptcLength = 0;
	        			fin.close();
	        			return false;
	        		}
	        	}
	        }
	        fin.close();       
        } catch(Exception e) {   
        	e.printStackTrace();
			if(fin!=null){
				try {
					fin.close();
				}catch (Exception e2){}
			}
        	return false;
        }   
		
		return true;
	}
	
	boolean compareByteArray(byte desc[], byte src[]) {
		for (int i=0; i<src.length; i++) {
			if (desc[i] != src[i]) {
				return false;
			}
		}
		
		return true;
	}
	
	byte[] sub(byte buffer[], int start) {
		byte[] subBuffer = new byte[buffer.length - start];
		for (int i=0; i<subBuffer.length; i++) {
			subBuffer[i] = buffer[i+start];
		}
		
		return subBuffer;
	}
	
	byte[] sub(byte buffer[], int start, int len) {
		byte[] subBuffer = new byte[len];
		for (int i=0; i<len; i++) {
			subBuffer[i] = buffer[i+start];
		}
		
		return subBuffer;
	}
	
	byte[] get8BIMHeader(byte buffer[], byte type[], int len) {
		int pos = 0;
		while (pos < len) {
			if (compareByteArray(sub(buffer, pos), JpegParam.PHOTOSHOP_8BIM.getBytes())) {
				if (compareByteArray(sub(buffer, pos+4), type)) {
					byte nNameSize = buffer[pos+6];

					if (nNameSize % 2 == 0) {
						nNameSize += 1;
					}
					return sub(buffer, pos+4+2+1+nNameSize);
				}
			}
			pos++;
		}
		return null;
	}
	
	int setTagList() {
		mTagList.clear();
		int len = 0, pos = 0, count = 0;
		byte marker = 0x1c, record = 0, tag = 0;
		
		while (pos < mIptcLength - 5) {
			if (mIptc[pos] == marker) {
				record  = mIptc[pos+1];
				tag = mIptc[pos+2];
				len = ((int)(mIptc[pos+3] & 0xFF)<<8) + (int)(mIptc[pos+4] & 0xFF);
				int xlen = 0;
				
				if (len > 0x7FFF) // extended dataset tag
				{
					xlen = (len & 0x7FFF);
					len = 0;
					for (int i=0; i<xlen; i++) {
						len = (len<<8) + (int)(mIptc[pos+i]& 0xFF);
					}
				}
							
				TagInfo idx = new TagInfo();
				
				idx._marker = marker;
				idx._record = record;
				idx._tag = tag;
				idx._len = len;
				idx._extend = (byte) (xlen>0 ? 1 : 0);
				idx._position = pos;

				mTagList.add(idx);

				count++;
				pos = pos + 5 + len + xlen;
			} else {
				break;
			}
		}
		return count;
	}
	
	byte[] getTag(byte record, byte tag) {
		int nCount = mTagList.size();
		byte[] result = null;
		if (0<record && record<10 && mIptc!=null && nCount>0) {
			for (int i=0; i<nCount; i++) {
				TagInfo idx = mTagList.get(i);
				if (record == idx._record && tag == idx._tag) {
					int xlen = 0;
					if (idx._extend == 1) {
						xlen = ((int)(mIptc[idx._position+3] & 0xFF)<<8) + (int)(mIptc[idx._position+4] & 0xFF);
						if (xlen > 0x7FFF) {
							xlen &= 0x7FFF;
						} else {
							xlen = 0;
						}
					}
					result = sub(mIptc, idx._position + 5 + xlen, idx._len);

					break;
				}
				
			}
		}
		return result;
	}
	
	String getStrTag(byte record, byte tag) {
		if (mIptc != null) {
			byte[] data = getTag(record, tag);
			if (data != null) {
				try {
					return new String(data, "GBK");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return "";
				}
			}
		}
		return "";
	}
	
}
