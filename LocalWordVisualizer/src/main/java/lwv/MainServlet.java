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

		LocalWord localWord = new LocalWord();
		if (query.endsWith("(D)")) {
			localWord.setType(LocalWord.WordType.DESCRIPTION);
		} else if (query.endsWith("(H)")) {
			localWord.setType(LocalWord.WordType.HASHTAG);
		} else {
			localWord.setType(LocalWord.WordType.TWEET);
		}
		localWord.setValue(query.split("\\s")[0]);

		int index = localWordList.indexOf(localWord);
		if (index != -1) {
			localWord = localWordList.get(index);
			request.setAttribute("selectedLocalWord", localWord);
		} else {
			request.setAttribute("notFoundMessage", "Word could not be found.");
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