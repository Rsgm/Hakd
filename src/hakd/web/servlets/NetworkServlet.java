package hakd.web.servlets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NetworkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public NetworkServlet() {

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println(request.getRequestURL());
		System.out.println(request.getRequestURI());
		response.setContentType("text/html");

		String website = "thepiratebay";

		PrintWriter line = response.getWriter();
		FileInputStream readFile = new FileInputStream(
				new File(".").getPath() + "/src/com/github/rmirman/hakd/web/websites/" + website + ".html");

		DataInputStream input = new DataInputStream(readFile);
		BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
		String strLine;
		while ((strLine = buffer.readLine()) != null) {
			line.print(strLine);
		}
		buffer.close();
		//case statements or if's to find the network website
		//findnetwork(url.parse(2)).getweb/*(int)*/ open that html file
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
		System.out.println("post - " + request.getParameter("search")); // works great
	}

}
