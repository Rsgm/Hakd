package hakd.websites.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public StoreServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		//response.sendRedirect("/files/Main/WebContent/ThePirateBay.html");
		PrintWriter out = response.getWriter();
//		out.print(s)
		out.print("<html><head><title>" + "Hello World!" + "</title></head>");
		out.print("<body><h1>test");
		out.print("</h1></body></html>");
		
		//response.set

		System.out.println(request.getRequestURL());
		System.out.println(request.getRequestURI());

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
