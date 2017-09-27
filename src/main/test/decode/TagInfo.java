package decode;

public class TagInfo {
	public byte _marker; // 1C marker
	public byte _record;
	public byte _tag;
	public byte _extend; // extended flag
	public int _len;
	public int _position;
}
