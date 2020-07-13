package mypackage;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class's purpose is to make page access in the index file using <tt>RandomAccessFile</tt>
 *@param pageSize the size of the page given by MyEditor
 *@param fileName the path of the file given by MyEditor
 *@para curPage the number of the current page that FilePageAccess is accessing
 *@author Georgios Frangias
 */

public class FilePageAccess {
	String fileName;
	int pageSize;
	int curPage;
	public FilePageAccess(String fileName, int pageSize) {
		this.fileName = fileName;
		this.pageSize = pageSize;
	}

	public FilePageAccess(String fileName, int pageSize, int curPage) {
		this.fileName = fileName;
		this.pageSize = pageSize;
		this.curPage = curPage;
	}

	/**
	 * It reads a data page from the index file
	 * @param pageNum the number of the page needed to access
	 * @return byte array of the data page 
	 * @throws IOException
	 */
	public byte[] read(int pageNum) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(this.fileName, "r");
		byte b[];
		b = new byte[this.pageSize];
		raf.seek(pageNum*this.pageSize);
		this.curPage = pageNum;
		raf.read(b);
		raf.close();
		return b;
	}

	/**
	 *  It writes the data page in the index file
	 * @param pageNum the number of the page needed to access
	 * @param b byte array of size equal to the page size
	 * @throws IOException
	 */

	public void write(int pageNum, byte[] b) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(this.fileName, "rw");
		raf.seek(pageNum*this.pageSize);
		this.curPage = pageNum;
		raf.write(b);
		raf.close();
	}

	/**
	 * It reads the next data page from the index file
	 * @return byte array of the data page
	 * @throws IOException
	 */
	public byte[] readNext() throws IOException {
		RandomAccessFile raf = new RandomAccessFile(this.fileName, "r");
		byte b[];
		b = new byte[this.pageSize];
		this.curPage++;
		raf.seek(this.curPage*this.pageSize);
		raf.read(b);
		raf.close();
		return b;
	}

	/**
	 * It checks if the next data page exists
	 * @return <i>true</i> if the next data page exists
	 * <p><i>false</i> if there is no next data page
	 * @throws IOException
	 */
	public boolean hasNext() throws IOException {
		RandomAccessFile raf = new RandomAccessFile(this.fileName, "r");

		raf.seek((this.curPage+1)*this.pageSize);
		if(raf.read() < 0) {
			raf.close();
			return false;
		}
		else {
			raf.close();
			return true;
		}
	}

	/**
	 * It checks if the previous data page exists
	 * @return <i>true</i> if the previous data page exists
	 * <p><i>false</i> if the current data page is the first 
	 * @throws IOException
	 */
	public boolean hasPrevious() throws IOException {
		if((this.curPage-1)*this.pageSize<0)
			return false;
		else
			return true;	
	}
}
