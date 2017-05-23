package lwv;

import lwv.data.LocalWord;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;


@WebServlet("/")
public class MainServlet extends HttpServlet {
	private Config config;


	@Override public void init() throws ServletException {
		super.init();
		config = Config.getInstance();
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<LocalWord> localWordList = config.getLocalWordList();
		String query = request.getParameter("query");
		String[] queryParts = query.split(" ");
		if (queryParts.length > 1) {
			LocalWord localWord = new LocalWord();
			switch (queryParts[1]) {
				case "(D)":
					localWord.setType(LocalWord.WordType.DESCRIPTION);
					break;
				case "(H)":
					localWord.setType(LocalWord.WordType.HASHTAG);
					break;
				default:
					localWord.setType(LocalWord.WordType.TWEET);
					break;
			}
			localWord.setValue(queryParts[0]);

			int index = localWordList.indexOf(localWord);
			if (index != -1) {
				localWord = localWordList.get(index);
				request.setAttribute("selectedLocalWord", localWord);
			}
		}
		request.setAttribute("localWordList", localWordList);
		RequestDispatcher dispatcher = request.getRequestDispatcher("main.jsp");
		dispatcher.forward(request, response);
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("localWordList", config.getLocalWordList());
		RequestDispatcher dispatcher = request.getRequestDispatcher("main.jsp");
		dispatcher.forward(request, response);
	}
}