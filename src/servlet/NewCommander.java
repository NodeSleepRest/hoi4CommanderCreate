package servlet;

import static java.nio.file.StandardCopyOption.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class NewCommander
 */
@WebServlet("/NewCommander")
public class NewCommander extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		List<String> traitValueList = Arrays.asList("old_guard","brilliant_strategist","inflexible_strategist","politically_connected","career_officer","trait_cautious","trait_reckless","media_personality","harsh_leader","war_hero","bearer_of_artillery","infantry_officer","cavalry_officer","armor_officer","JAP_samurai_lineage","JAP_communist_sympathizer","organizer","panzer_leader","cavalry_leader","trait_engineer","infantry_leader","naval_invader","commando","trickster","skilled_staffer","panzer_expert","combined_arms_expert","cavalry_expert","fortress_buster","scavenger","infantry_expert","ambusher","invader_ii","naval_liason","skirmisher","paratrooper","camouflage_expert","expert_improviser","guerilla_fighter","desert_fox","swamp_fox","hill_fighter","trait_mountaineer","urban_assault_specialist","ranger","jungle_rat","winter_specialist","adaptable");
		List<String> traitNameList = Arrays.asList("古典派","華麗なる戦略家","頑固な戦略家","政治的縁故","経歴ある士官","慎重","無謀","メディアの有名人","厳しい指揮官","戦争の英雄","砲弾の運び手","歩兵士官","騎兵士官","機甲士官","武士の血筋","共産主義のシンパ","組織者","装甲部隊の指揮官","騎兵指揮官","工兵","歩兵指揮官","侵略者","コマンドー","トリックスター","優れた参謀","戦車の名手","諸兵科連合の名手","騎兵の名手","要塞の破壊者","廃品回収者","歩兵の名手","伏兵","水陸両用","海軍との連絡","散兵","空挺兵","擬装の名手","臨機応変の名手","ゲリラ戦士","砂漠の狐","沼地の狐","丘陵の戦士","山岳の専門家","都市攻撃の専門家","レンジャー","密林の鼠","冬季戦の専門家","適応");
		List<String> countryValueList = Arrays.asList("CUB", "ETH", "JAP", "ICE", "IRE");
		List<String> countryNameList = Arrays.asList("キューバ", "エチオピア", "大日本帝国", "アイスランド", "アイルランド");

		session.setAttribute("traitValueList", traitValueList);
		session.setAttribute("traitNameList", traitNameList);
		session.setAttribute("countryValueList", countryValueList);
		session.setAttribute("countryNameList", countryNameList);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/newCommander.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		request.setCharacterEncoding("UTF-8");

		FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
		// 各種パラメータ
		String name = request.getParameter("name");
		String skill = request.getParameter("skill");
		String attack = request.getParameter("attack");
		String defense = request.getParameter("defense");
		String planning = request.getParameter("planning");
		String logistics = request.getParameter("logistics");

        String[] traitValues = request.getParameterValues("trait");

        // 国ファイルサーチ＆コピー
        String countryValue = request.getParameter("country");
        dumpFile(new File("D:\\"), countryValue, request);

		try {
			fos = new FileOutputStream(new File(".").getAbsoluteFile().getParent() + "\\" + (String) session.getAttribute("writeFile"), true);

            //BOM付与
            fos.write(0xef);
            fos.write(0xbb);
            fos.write(0xbf);

            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);

            bw.write("\r\n");
            bw.write("create_corps_commander = {\r\n");
            bw.write("	name = \"" + name + "\"\r\n");
            bw.write("		picture = \"Portrait_Japan_Kiichiro_Higuchi.dds\"\r\n");
            bw.write("	traits = { ");
            if(traitValues != null) {
	            for(int i=0; i<traitValues.length; i++) {
	            	bw.write(traitValues[i] + " ");
	            }
            } else {
            	bw.write("*");
            }
            bw.write("}\r\n");
            bw.write("	skill = " + skill + "\r\n");
            bw.write("	attack_skill = " + attack + "\r\n");
            bw.write("	defense_skill = " + defense + "\r\n");
            bw.write("	planning_skill = " + planning + "\r\n");
            bw.write("	logistics_skill = " + logistics + "\r\n");
            bw.write("}" + "\r\n");
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/newCommander.jsp");
		dispatcher.forward(request, response);
	}

	public void dumpFile(File file, String fileName, HttpServletRequest request){

        // ファイル一覧取得
        File[] files = file.listFiles();

        if(files == null){
            return;
        }

        for (File tmpFile : files) {

            // ディレクトリの場合
            if(tmpFile.isDirectory()){

                // 再帰呼び出し
            	dumpFile(tmpFile, fileName, request);

            // ファイルの場合
            }else{

            	if(tmpFile.getName().startsWith(fileName + " - ") && tmpFile.getName().endsWith(".txt") && !tmpFile.getName().endsWith("_AT.txt")) {
            		HttpSession session = request.getSession();
                    Path destPath = Paths.get(new File(".").getAbsoluteFile().getParent() + "\\" + tmpFile.getName());
                    System.out.println(new File(".").getAbsoluteFile().getParent() + tmpFile.getName());

            		try {
						Files.copy(tmpFile.toPath(), destPath, REPLACE_EXISTING);
						session.setAttribute("writeFile", tmpFile.getName());
						System.out.println(tmpFile.getName());
					} catch (IOException e) {
						// 自動生成された catch ブロック
						e.printStackTrace();
					}
            	}
            }
        }
    }

}
