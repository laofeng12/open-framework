package org.ljdp.ui.servlet3;

import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class MultipartUtil {
	private static final String FILENAME_KEY = "filename=\"";

	/**
	 * Gets the original filename of a {@code Part}. The filename is encoded in
	 * the {@code content-disposition} header of the {@code Part} as the value
	 * of {@code filename} parameter.
	 * 
	 * @param part
	 *            The {@code Part} object.
	 * @return The filename of the part, or {@code null} if the part doesn't
	 *         contain a file.
	 */
	public static String getFileNameFromPart(Part part) {
		String cdHeader = part.getHeader("content-disposition");
		if (cdHeader == null) {
			return null;
		}
		for (String s : cdHeader.split("; ")) {
			if (s.startsWith(FILENAME_KEY)) {
				// Some stupid browers, e.g. IE, put the full file path on the
				// client machine as the value, so we have to extract the
				// filename
				// out from it.
				String path = s
						.substring(FILENAME_KEY.length(), s.length() - 1)
						.replaceAll("\\\\", "/");
				return path.substring(path.lastIndexOf("/") + 1);
			}
		}
		return null;
	}

	/**
	 * Returns the value of a request parameter from the given
	 * {@code HttpServletRequest} as a {@code String}, or {@code null} if the
	 * parameter does not exist. This method can handle request of type both
	 * {@code application/x-www-form-urlencoded} and {@code multipart/form-data}
	 * .
	 * 
	 * @param req
	 *            The request object.
	 * @param name
	 *            The name of the parameter.
	 * @param charset
	 *            The name of charset for parsing bytes to string if the request
	 *            if of type {@code multipart/form-data}. An improper charset
	 *            might lead to messy code in the returned string.
	 * @return The velue of the parameter.
	 */
	public static String getParameter(HttpServletRequest req, String name,
			String charset) {
		// First assume the request is of type
		// application/x-www-form-urlencoded.
		String value = req.getParameter(name);
		if (value != null) {
			return value;
		}
		// Trying to handle the request as a multipart/form-data type.
		try {
			Part part = req.getPart(name);
			if(part == null) {
				return null;
			}
			Reader in = new InputStreamReader(part.getInputStream(), charset);
			StringBuilder sb = new StringBuilder();
			char[] buffer = new char[256];
			int read = 0;
			while ((read = in.read(buffer)) != -1) {
				sb.append(buffer, 0, read);
			}
			in.close();
			return sb.toString();
		} catch (Exception ex) {
			return null;
		}
	}
}
