package lwv;

import lwv.data.LocalWord;

import javax.servlet.ServletException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Config {
	private static Config config;

	private List<LocalWord> localWordList;


	public static Config getInstance() throws ServletException {
		if (config == null) {
			config = new Config();
			config.buildLocalWordList();
		}
		return config;
	}


	public List<LocalWord> getLocalWordList() {
		return localWordList;
	}


	public void setLocalWordList(List<LocalWord> localWordList) {
		this.localWordList = localWordList;
	}


	private void buildLocalWordList() throws ServletException {
		String line;
		this.localWordList = new ArrayList<>();

		try (InputStream in = this.getClass().getResourceAsStream("/localWords.csv")) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			while ((line = br.readLine()) != null) {
				String[] lineParts = line.split(",");
				LocalWord localWord = new LocalWord(lineParts);
				localWordList.add(localWord);
			}
		} catch (IOException e) {
			throw new ServletException("Could not load file with local words.", e);
		}
	}
}
