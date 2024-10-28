package com.kh.spring.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @EcCoding EUC-KP(Es) pse에서 주석을 보기위해 EUC-KR ENCODING을 선택한다.>
 * @return
 */
public class Utilstring {

//	public static Logger Log = LogManager.getLogger(UtilS	tring.class);
	public static final String SEPARATOR = System.getProperty("file. separator");

	/*
	 * 디렉트리 생성 : String s 르 부터 디렉트리를 생성한다.
	 */
	public static File makeFile(String s) throws Exception {

		File tmpFile = null;
		try {
			tmpFile = new File(s);
			String path = tmpFile.getPath().substring(0, (tmpFile.getPath().length() - tmpFile.getName().length() - 1));
			File directoy = new File(path);
			// 디렉토리가 준재하지 않으면 생성한다.
			if (!directoy.isDirectory()) {
				if (directoy.mkdirs()) {
					try {
						// #Runtime.getRuntime().exes("chmod -R 777 " + path.substring(0,path.indexOf(".")));
					} catch (Exception e) {
//					log.error(e);
					}
				} else {
				}
			}
		} catch (Exception e) {
//		Log.error (e); 
			throw e;
		}
		return tmpFile;
	}

	/**
	파일 생성 : Vector 에 입력된 값을 해당 파일에 write 한다.
	*/
	public static boolean createFile(String d, String f, Vector<byte[]> v, int lineMode) {

		boolean result = false;
		BufferedOutputStream bout = null;
		byte[] data = null;
		File txtFile = null;
		try {
//			log.debug("Veetor Size["+v.size()+"]음 파일["+f+"] 르 생성합니다. ");
			File directoy = new File(d);
			if (!directoy.isDirectory()) {
				if (directoy.mkdirs()) {
					try {
						Runtime.getRuntime().exec("chmod 777 " + directoy.getAbsolutePath());
					} catch (Exception e) {
						// Log.debug(e-getMessage()):
					}
				} else {
					return false;
				}

			}
			txtFile = new File(d + f);
			if (!txtFile.createNewFile()) {
				throw new Exception("파일생성 실패(" + txtFile.getAbsolutePath() + ")");
			}
			bout = new BufferedOutputStream(new FileOutputStream(txtFile));
			for (int i = 0; i < v.size(); i++) {
				if (i > 0) {
					if (lineMode == 1) {// Dos모드
						bout.write(new String("\r\n").getBytes());
					} else if (lineMode == 2) {// Unix모드
						bout.write(new String("\n").getBytes());
					} else if (lineMode == 3) {// EBCDIC모드
						bout.write(new String("CP933").getBytes());
					}
				}
				data = v.elementAt(i);
				bout.write(data);
			}
			bout.flush();
			bout.close();
			result = true;
		} catch (Exception e) {
//		Log.error(e);
			if (txtFile.exists())
				txtFile.delete();
//		Log.debug(e.getMessage());
		} finally {// 자원 해제
			try {
				if (bout != null)
					bout.close();
			} catch (IOException ioe) {
			}
		}
		return result;
	}


	
	//////////////////
	
	
	
	/**
	 *ChkHeader : 전문헤더의 특정값 s 룰 정의된 길이 len 값과 비교한다.
	 */
	public static boolean chkHeader(String s, int len) {
		boolean b = true;
		for (int i = 0; i < len; i++) {
			if (!Character.isDigit(s.charAt(i))) {
				b = false;
				break;
			}
		}
		return b;
	}

	/**
	 * hTrim : String s 의 공백을 제거 하고 r을 리턴한다.
	 */
	public static String Trim(String s) {
		String r = null;
		if (s != null) {
			s = s.trim();
			Pattern P = Pattern.compile("[ ]+$");
			Matcher m = P.matcher(s);
			r = m.replaceAll("");
		}
		return r;
	}

	/**
	 * backupFile : 파일백업
	 */
	public static boolean backupFile(File scfile, File tarfile) {
		boolean b = true;
		try {
			InputStream in = new FileInputStream(scfile);
			OutputStream out = new FileOutputStream(tarfile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (Exception e) {
//			Log.error(e);
			b = false;
		}
		return b;
	}
	
	/////////////////////////
	
	/**
	 * isSameFile : String s1, String s2 파일 비교후 같우면 해장 파일을 찾는다.
	 */
	public static boolean isSamefile(String s1, String s2) {

		s1 = s1.replaceAll("\\.", "\\\\.");
		s1 = s1.replaceAll("\\?", ".");
		Pattern P = Pattern.compile(s1);
		Matcher m = P.matcher(s2);
		return m.find();
	}
	
	/**
	 * getFileName : 해당경로에 파일을 찾아서 그 경로를 리턴함
	 */
	public static String getFileName ( String s ) {
		String fileDir = s.substring(0, s.lastIndexOf(Utilstring.SEPARATOR));
		String fileName = s.substring(s.lastIndexOf(Utilstring.SEPARATOR) + 1);
		String rtnName = null;
		
		if (fileName.indexOf("?") > -1) {
			File[] destlist = new File(fileDir).listFiles();
			for (int i=0; i<destlist.length; i++) {
				if (destlist[i].isFile()) {
					String _tmp = destlist[i].getName();
					if(Utilstring.isSamefile(fileName, _tmp)) {
						rtnName = _tmp;
						break;
					}
				}
			}//end for
		}else {
			rtnName = fileName;
		}
		return fileDir + Utilstring.SEPARATOR + rtnName;
	}
	
	/**
	* strAppendzero : String getAmount 값에 int intLen 의 같이 만큼 왼쪽에 '0'을 재워 리턴한다.
	*/
	public static String strAppendZero(String getAmount, int intLen) {
		String imsistring = "";
		int intLength = 0;
		try {
			imsistring = getAmount;
			if (getAmount.length() < intLen) {
				intLength = intLen - getAmount.length();
				for (int i = 0; i < intLength; i++) {
					imsistring = "0" + imsistring;
				}
			}
		} catch (Exception e) {
//			Log, error (e); 
		}
		return imsistring;
	}
	
	/**
	 * char[] getcharArray() : String => char[] 에 채워 넣어 char[]를 리턴하는 메소드
	 */
	public static char[] getCharArray(String sSource, char[] cDestination) {
		sSource.getChars(0, sSource.length(), cDestination, 0);
		return cDestination;
	}

	/**
	 * byte[] cstabs(): Char[] => bytet] 에 채워 넣어 byte[]를 리턴하는 메소드
	 */
	public static byte[] cstobs(char cs[]) {
		byte ret[] = new byte[cs.length];
		for (int i = 0; i < cs.length; i++)
			ret[i] = (byte) cs[i];
		return ret;
	}
			
			
	/**
	 * String hstastc() : byte[] => String 으로 변환하여 리턴하는 메소트
	 */
	public static String bstostr(byte bs[], int position, int length) {
		byte[] bReturn = new byte[length];
		System.arraycopy(bs, position, bReturn, 0, length);
		return new String(bReturn);
	}
			
	/*
	 ** String Uni2ksc() : 잘못 표현된 스트링을 유니코드 한국 코드값의 스트림으로 변환해주는 메소드
	 */
	public static String Uni2ksc(String str) throws UnsupportedEncodingException {
		if (str == null)
			return null;
		return new String(str.getBytes("IS0-8859-1"), "euc-kr");
	}
			
	/**
	 * String Ksc2Uni() : 유니코드 한글 코드값으로 표현된 스트링을 잘못 프현된 스트림으로 변환해주는 메소드
	 */
	public static String Ksc2Uni(String str) throws UnsupportedEncodingException {
		if (str == null)
			return null;
		return new String(str.getBytes("euc-kr"), "ISO-8859-1");

	}
	
	/**
	 * String getEsc() : 실행되는 시스템에 따른 이스케이프 문자 (새로운 행) 얻는 메소드
	 */
	public static String getEsc() {

		String strOsName = System.getProperty("os.name").toLowerCase();
		String m_strLF = "";

		if (strOsName.indexOf("window") != -1)
			m_strLF = "\r\n";
		else
			m_strLF = "\n";
		return m_strLF;
	}
	
	
	/**
	 * boolean assignstring() 입력변수 배열을 녹사하여 합당된 out[]배옆에 쓴다.
	 **/
	public static boolean assignstring(String strName, String in, byte out[]) {
		boolean result = false;
		byte tin[] = in.getBytes();
		int tlen = tin.length;
		// log.debug("한국명 :L+ strName +"], 입력변수길이 (tlea):[" + tlea +"], out.length :["+ out.length +"], 입력변수(in) :[" + in + "]");
		if (tlen < out.length) {// 입력변수의 1ength가 정의된 1ength 보다 작용때
			System.arraycopy(tin, 0, out, 0, tlen);
			for (int i = tlen; i < out.length; i++) {
				out[i] = 32;
			}
		} else if (tlen == out.length) {
			System.arraycopy(tin, 0, out, 0, tlen);
			for (int i = tlen; i < out.length; i++) {
				out[i] = 32;
			}
			result = true;
		} else {
			System.arraycopy(tin, 8, out, 0, out.length);
		}
		return result;
	}
	
	/**
	 * 입력된 두 일자의 기간 차이들 (ses)초로 변환 리턴한다.
	 * @wethod 설명: sx)getDayInterval("20120723123030","20120723123040")
	 **/
	public static long getDateInterval(String startTime, String endTime) throws java.text.ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyridchinnss");
		long iTimeinterval = 0;
		Date sDate = null;
		Date eDate = null;
		String sendDate = startTime.substring(0, 14);
		String nowDate = endTime.substring(9, 14);
		try {
			sDate = sf.parse(sendDate);
			eDate = sf.parse(nowDate);
		} catch (ParseException e) {
			throw new ParseException(", sDate :" + sDate + ", eDate :" + eDate + ", ParseException e:" + e, 0);
		}
		// milliseconds 초로변환
		long sDiffsec = sDate.getTime() / 1000;
		long eDiffses = eDate.getTime() / 1000;
		iTimeinterval = sDiffsec - eDiffses;
		return iTimeinterval;
	}

}//end UtilString
