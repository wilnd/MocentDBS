package decode;



public class ExNewsData {
	
	public String picFileName;
	public String title;
	public String content1, content2, content3, content4, content5;
	public int newsId;
	
	public ExNewsData() {
	}
	
	public ExNewsData(String picFileName, String title, String content1, 
					String content2, String content3, String content4, String content5) {
		this.picFileName = picFileName;
		this.title = title;
		this.content1 = (content1!=null)?content1:"";
		this.content2 = (content2!=null)?content2:"";
		this.content3 = (content3!=null)?content3:"";
		this.content4 = (content4!=null)?content4:"";
		this.content5 = (content5!=null)?content5:"";
	}

	public String getPicFileName() {
		return picFileName;
	}

	public String getTitle() {
		return title;
	}

	public String getContent1() {
		return content1;
	}
	
	public String getContent2() {
		return content2;
	}
	
	public String getContent3() {
		return content3;
	}
	
	public String getContent4() {
		return content4;
	}
	
	public String getContent5() {
		return content5;
	}
	
	public static ExNewsData decodeJpeg(String jpgFilePath) {
		JpegDecoder jpegDecoder;
		jpegDecoder = new JpegDecoder();
		jpegDecoder.loadJpegFile(jpgFilePath);
			
		String title = jpegDecoder.getCaption();
		String space = "        ";
		String content1, content2, content3, content4, content5;
		
		content1 = jpegDecoder.getQuckMsgLine(0);

		content2 = jpegDecoder.getQuckMsgLine(1);

		
		content3 = jpegDecoder.getQuckMsgLine(2);

		
		content4 = jpegDecoder.getQuckMsgLine(3);

		
		content5 = jpegDecoder.getQuckMsgLine(4);

		
		ExNewsData exNewsData = new ExNewsData(jpgFilePath, title, content1, content2, content3, content4, content5);
		System.out.println();
		return exNewsData;
	}

	@Override
	public String  toString(){
		System.out.println("title:"+this.title+"content:"+this.content1+this.content2+this.content3+this.content4+this.content5);
		return "1";
	}
}
