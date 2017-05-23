import com.google.gson.Gson;
import lwv.data.LocalWord;
import lwv.data.Probability;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class LwvTest {
	@Test
	public void createJson() throws Exception {
		List<LocalWord> lwList = new ArrayList<>();
		LocalWord lw = new LocalWord();
		lw.setType(LocalWord.WordType.HASHTAG);
		lw.setValue("mweeh");
		Probability prob1 = new Probability();
		prob1.setProvince(Probability.Province.GR);
		prob1.setValue(0.000002);
		Probability prob2 = new Probability();
		prob2.setProvince(Probability.Province.FR);
		prob2.setValue(0.000007);
		List<Probability> probabilityList = new ArrayList<>();
		probabilityList.add(prob1);
		probabilityList.add(prob2);
		lw.setProbabilities(probabilityList);
		lwList.add(lw);

		Gson gson = new Gson();
		System.out.println(gson.toJson(lwList));

	}
}
