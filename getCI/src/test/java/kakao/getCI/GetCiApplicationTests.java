package kakao.getCI;

//import kakao.getCI.aop.AopConfig;
import kakao.getCI.aop.SimpleLogAop;
import kakao.getCI.com.shinhan.security.imple.SAProperty;
import kakao.getCI.com.shinhan.security.imple.SASimpleAuthAction;
import kakao.getCI.com.shinhan.security.imple.SimpleAuthTask;
import kakao.getCI.com.shinhan.security.simpleauth.SAConst;
import kakao.getCI.com.shinhan.security.simpleauth.exception.SASimpleAuthException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;

@Slf4j
@Import(SimpleLogAop.class)
@SpringBootTest
class GetCiApplicationTests {

	public String APP_ID 		 = "TEST_ALIAS";
	public String APP_ALIAS 	 = "SA_ALIAS";
	public String APP_VERSION 	 = "1.0";
	public String AUTH_TYPE 	 = "3";

	public String CUSNO			 = "0987654321";
	public String ID 			 = "1234567890";
	public long   currentTime    = 1630642197023L;

	// 클라이언트 고정값
	public String strChallenge   = "3E74C931086CF7B52FA22921E4718134433A0CE65045740540E303CA46D492F86D0F55667D5EA1EADDB68D0544F0F2A4B99F62498C4148F5E0E744DF710C0297";
	public String PUBKEY 		 = "30820122300D06092A864886F70D01010105000382010F003082010A0282010100997A4DE16E426664DB117B7EEBAD0F17BF1C317EEE8A4D1D441C60CD26598B03B3BCA3277BDCB0FDC26D3541575D14510103D7A127829A2CF2D2FAC6F61E1AD4D4685945C3AFF3F70B4261CBA77D07A60CB52217A25A1E7F412EBA83D44B4D5F5EC84D7B6AB438814A767637E2FBAFB73EE6442D85AF15A38DE59324ECB65FE58C864AA5B367DAD43C739C8B6562AE013406CF1985DD065FF5BB0D9C23CF118902ED7CB418718BA8B839340583A6BF9058AE04FDFD22AA2E265B5A83EBCB0397EB328D4A4FCC7F29AAAE528C3D18AE71475D3E574E3B53ABCD2747098EE2291F235C578788B72715768F2A6371C495693F543F15DDB2F518161BB7801307D0FD0203010001";
	public String PRIKEY 		 = "308204BE020100300D06092A864886F70D0101010500048204A8308204A40201000282010100997A4DE16E426664DB117B7EEBAD0F17BF1C317EEE8A4D1D441C60CD26598B03B3BCA3277BDCB0FDC26D3541575D14510103D7A127829A2CF2D2FAC6F61E1AD4D4685945C3AFF3F70B4261CBA77D07A60CB52217A25A1E7F412EBA83D44B4D5F5EC84D7B6AB438814A767637E2FBAFB73EE6442D85AF15A38DE59324ECB65FE58C864AA5B367DAD43C739C8B6562AE013406CF1985DD065FF5BB0D9C23CF118902ED7CB418718BA8B839340583A6BF9058AE04FDFD22AA2E265B5A83EBCB0397EB328D4A4FCC7F29AAAE528C3D18AE71475D3E574E3B53ABCD2747098EE2291F235C578788B72715768F2A6371C495693F543F15DDB2F518161BB7801307D0FD0203010001028201000DE31D2CEAB72360B8D9CA1CDFE256DD45339DDEE862A73BA0B018AA37701EBE05036A97E603401896011FB27BDF363966281CD8BE247922DC0DEF1715A9FFAF8650B2D33733B29585F549B80DBDA87583ECD7DADF9BDBEA93D1509B21187735CBF3BFDF0CF6A5AE3D389ED5CBBA0E5A3C5CC58A80F028BB15D9D208A34A953EA21394EF849A67A96CD32A24973C70A0E7EB6A28ED26BA9BB520B07D8AFE5E918A95D887DEF945147E255CB3BA56B5708EA073F5C88807DB071ABF05CCAC385FB7A53D98EC3736C864EC78859832FC71A04111AB3E890CC8B8D06C380ECE7398D0B80259516453A7D038D6502FCBC1B754D724A966D711EB02A62C8B7103EE8102818100D506BA94982C7927426AD5172DC3B03A368474BA3369AEF46C269C88EC688F0AB1B0F09A103F4769B24F43B9F77BB863AAE43233798D601FED7E62826BF7CD602E2CC3E49E424C5E3BB5AA7FE07FE9333B597070298CE88B904445AC068F104D23E54839BD11110DDAEDAE7BB47DEE15CBC3FF6A68011CD88B3764DAAFA0BC3102818100B87052B7AD68F53D3B1C13E041979265D558D1DA632111B96D969DE0361BEE29FFC1E537AB126791DB70C420C39475A2CA3CB7AB7F19FA675EDCE07F65A9B91CE2B64CB6DC9E2FB40C3F2263E146FA66DB1DB5B6C8A126C1057E33822EDA8C4A15885E3F8F7C144BFEB8C5DADEB138127FCDA88CD4900D8454E0BB389E3A4A8D0281810091F73C5D992FD00C309C574ABC96FBE7C777572C4712414945D9E7248328D9EAAFFAD7C21923E2E0EFC7145716D6F56F85A73B26BE413AFE659356189BB67E494B1A6D57F780E22727AEC1174253230D71BB4529F5AE91A11FC7481E299859C921B1779DE8890B307D34A37C2C1EA5C6D58A8536F9F0D93D1F976349DD51BBE1028181008684EDDFA76B90CA3AED6C95B8614B565877B27EFCA3E6E07E4C47137B837D7E465A6DCADF4BCA1C389793846E013E971FB78F2256CE64B2B61E94E2760C86C5C998BA74F88DAB2F3F6B60B2C660D54AE56F52D4609A20C0D137CD6B9AAEAD796F2109D483DB349684CED107A43A4F3236E291973D855F466FFD84AF73C002150281807B8D8D2E59033CBE8BDEF90CF60AEE1C46D2559989910D32F76BCDA061F7484CA82F7251347093B2487A8C5A6DCF6B512023FECFD52390176E6F02D29836D0202B8A4D75516390BBEDD270082C83F4DFACAFBE0FBA8BE9AF6706E3B38BF8BCC4A08CF0D36C8198D99F26F4704791AB5407C63296F471FA4CF5C5959051B84FE3";
	public String UUID 			 = "uuid";
	HttpSession session;

	@Autowired
	SimpleAuthTask task;


	@BeforeEach
	void init()
	{
		SAProperty.setSA_Log(false);
		SAProperty.setSA_TBName("MBI_SIMPLEAUTH");
		SAProperty.setSA_ColumName("ID", "CUSNO", "PUBKEY", "UUID", "APP_ID", "TYPE", "STATUS", "REG_DTTM", "DROP_DTTM", "LAST_AUTH_DTTM");

		// log, sequence, index 생략
		SAProperty.setSA_DBPoolName("mbbPool");
		SAProperty.setSA_StatusValue("1", "9");

		session = new MockHttpSession();
		session.setAttribute(SAConst.TAG_CUSNO, CUSNO);
		session.setAttribute("sa_challenge", strChallenge);
		session.setAttribute("sa_authkey",AUTH_TYPE);


	}




	@Test
	void reg_init_test() throws SASimpleAuthException {
		String reqJson = "{\"tag\":\"3E01\",\"tmppub\":\"30820122300D06092A864886F70D01010105000382010F003082010A0282010100997A4DE16E426664DB117B7EEBAD0F17BF1C317EEE8A4D1D441C60CD26598B03B3BCA3277BDCB0FDC26D3541575D14510103D7A127829A2CF2D2FAC6F61E1AD4D4685945C3AFF3F70B4261CBA77D07A60CB52217A25A1E7F412EBA83D44B4D5F5EC84D7B6AB438814A767637E2FBAFB73EE6442D85AF15A38DE59324ECB65FE58C864AA5B367DAD43C739C8B6562AE013406CF1985DD065FF5BB0D9C23CF118902ED7CB418718BA8B839340583A6BF9058AE04FDFD22AA2E265B5A83EBCB0397EB328D4A4FCC7F29AAAE528C3D18AE71475D3E574E3B53ABCD2747098EE2291F235C578788B72715768F2A6371C495693F543F15DDB2F518161BB7801307D0FD0203010001\",\"appid\":\"TEST_ALIAS\"}";
		System.out.println("reqJson = " + reqJson);

//        HttpSession session = new MockHttpSession();
//        session.setAttribute(SAConst.TAG_CUSNO, CUSNO);
		String serverResult = task.registInit("3E01", reqJson, session);

	}

	@Test
	void reg_test()
	{

		String result = "{\"tag\":\"3E02\",\"signdata\":\"72A30A17A79B78317DA1569918B7818D71A976B4FC480301F74CDB6306C5A2914C1480E374DE081BE4CAEF45EE0EE29E2444D7A87958A1945D0E16B7DB8F66252769D0B90F95A143177C61DB9976A91CCD132500B1E3296349B69E59B8FB77A5C79B3184500E3FC4E06A8F2429AFBEF27A70CBEEDC39B6B46D3198BF6A8C44480CD029954CC217F9001031F21DEA1ED036E7876AC38547D27D7E5BE98C5CEC5702A8D9683A230F6FA7FAAB6AE728FE7A5BF7FA12478DC8DB3246FDEDFB066837C9795691CABF4753BA3F0CB2778EE49EA40AF4F58A3830B9BCFB683172C502CC67D627880EE39E699FFA60115B03E96F7E49EEE865325AD915EEB97770799CFF\",\"rd\":\"AF065436E9FF050CEA3E7A595605B0260BC68AF6AF83D577DB97D005A50A74A6EC836DC5967DA4F6F57CD3248E0368688AEFF656793BF4F6A95281EDD91639C4110BFC9A90695564E3E50A8A1F378E7CFA91C200FFE4766A29ECB795DA1854D8D571C317B6854E36A958A15598EE7D4534C961922D84CF71CB673289F89DBBED1206CB4E93EC8DB164C0EC24BE96BBA3EC07C9A44C02BA30DAF29B6063BAD5C1B2466C8E9209B4A10497D2D4F34A2F3E3A7988B823F0D3FE6A39ED8FF67DB05591073D18C13A72B8B5B1DF5971F968B68BECF74EFFD43C4FA9E0B33EA2E6E627AF4A5BC67FEBB9A532539337A77E211815C6A3C2EC577807C8D59D181A6ABBECFE00E471AE53960DC904F0FB453FA03A2B95261BF99563F634D2227278894F1F2F91C7DF96D39B258088F41AE5A8FD3BA15F831BF5978498BB36183698C81EE6A09ED8FC802348395DD36B02B7B128E3D5A2ECE5A93DBB5CCCE487D6A99E22E364739B0C46CFC8DBB58F98F02FAA4F41EE2A8BDB2E1B0897888DD5CFF124E61AFCA53D8BE1A99AA41FC3BCFDBE0541887B71AA120FCB509325BD41256C0E93BEACB65FEFA1CCDE1ACCA952086C6E558FB7D31BFD8B84DAF13BA4D2C0B0D3AE6F77CF7DD6283ECE2B17E7A4713F2755733730975EA514C77435416BF16F2AA2C7FE34955EBAF7E0AF63C5343EBC64176A433EE4A47D7628AD8FC37B945C6811A29DB93D991F0DBB14945D0D330BE38C9CF94D90E886170BCE3E7252C263B039BE0D46C511A13013C9BF1709442FE290A5F0B1C96EFFFAD7146831C36F7942740BD56787E3EE30F55D5533C0C6FC076A2FE3627EA1509C5485DCC68DE1706A68D48752D53C0745D2858BBF17F6692B10DB91B7EA5142F376E1D648FF25F2521056AEF40E18796C18C6CD4033A2946247055C737FA5686D018C2AAC009CCC45E7581327C4CA95A407EF932DF94AE1C576B846C263CD89067A04B7BCA4243B285DFCFD9E75957E260F30B531B3C232A0638B594826D468A109D29D634AFD7A23811C119EDE247DAD0D10E34597A8C3A6644A5175DDCB2867FD4386CE8A7D28D38261259A901264512A6D3CEC3A704E435A2CD5653E7440B73C06A846321B3351E11CA748A87A02994F5F0B0848DB481406D5F82B74B1A9A670B9B76A5EEBCF332C270EC0BFC48965588858E70907FD5B71BA7410F459FEFADDF5A5C07DF8024E35C6A203F8FFC253F0CBB7D2FE99F2E046526A337EA15EA1FEDDF39D21BC74CDF3FC\"}";
		System.out.println("result = " + result);
//        HttpSession session = new MockHttpSession();
//        session.setAttribute(SAConst.TAG_CUSNO, CUSNO);
//        session.setAttribute("sa_challenge", strChallenge);
		String serverResult = task.regist("3E02", result, session);
		System.out.println("serverResult = " + serverResult);
	}



	@Test
	void auth_init_test()
	{

		// ID값 reg에서 나온값으로 변경해줘야함!
		String client = "{\"appversion\":\"1.0\",\"model\":\"Samsung_SM-N976N\",\"ostype\":\"A\",\"osversion\":\"11\",\"saversion\":\"1.0.4\",\"type\":\"3\",\"appid\":\"TEST_ALIAS\",\"id\":\"11C948F98C8D41F42DBB89A25B4B99F43330CAE7CA68CEC621505F0B88F3057F\",\"tag\":\"3E11\",\"uuid\":\"6D8809FDD43FBDCAE4104D24C9D14FD320AFFAFEF20056D2EDCF69F4D6DF0BF52FED3A6DDAE96ECF8BB2ADA3005BA2072D94DC0478990974B7E0356FE7E3E8C1383A702204A995255CCB74C2593E8C390484B1A4FA24EA69E7EDC147A4773B2A74292485AE5C9627E840E0B96B9837F0D67B0C8B1E0F9B25C8C357604A38599C870AD6AAE7808790A94A0DAC1E4290B71D5D3EC4335AA1A1C101B7E8AB2CE018AC99403D7C65F22D4A1879859069EA0F6B8FB0390B13C1680A7D1C620556B0B49C6F1081A8831145B9FFE5977674D5E681A850850845646551295394E3B75440E6D3CB5F04567FE5D81A194F397526953102EB4DE0A8DE902FA494F0742891ED\"}";

		String serverResult = task.authorizeInit("3E11", client, session);
		System.out.println("serverResult = " + serverResult);

		String server_result = "{\"erroryn\":\"n\",\"appid\":\"TEST_ALIAS\",\"cusno\":\"0987654321\",\"tag\":\"3E02\",\"id\":\"11C948F98C8D41F42DBB89A25B4B99F43330CAE7CA68CEC621505F0B88F3057F\",\"type\":\"2\",\"uuid\":\"uuid\",\"status\":\"1\"}";
	}

	@Test
	void auth_test()
	{
		// String serverResult = "{\"erroryn\":\"n\",\"challenge\":\"3B2A2FBAE336D1ABC96AFBD2FFB03E3A5975F79910BE1BAD8341FEC830CB3BA0749289D019EAD918D22AAA9087B0A2BE0ABEFAEFC8478CA63ED7E2BEA78A3B07EC48A776F1461332E0522BB1CFA1F17B406850742F8B6A1E46C3AE6BCEB2A3A10C1426C8A14F8D4E44E45A17F1A24D72BCA68720E20FF38FB3DCA024E5E987A61D1777F9E29E9F16C2C9D118677407FF8570F77753E060800ED96E58F9D3FE4C2EA782FE801932C38EABF5469F4CB70FEA94D775C609668C927781A6ACA60E097344DFC07801347F23066D7411D9525276ACE2FE8173D74A47A5F91BF1865D240C2F046809560E88E2B097070AD3F317B61A0CE9D025DB8F865EF152CE19AB7F\",\"tag\":\"3E11\",\"time\":\"25F64F5A767037B388BD6EF9A2474E32DD1835B3066FDB8998A15D4EE60DDAF132A2C63652CCD209EA137E82752E51B67BFEADFB7730FE1B379F9A9D71BDB298EA4E8AB2782DD906C1E00E09DDE334250CA20B5029D89DBEC5A13AED51C5FFD53ED79D2DB940B4C01E39FCB0CD11F0048F96F9B8C1A9C2F3508A3C183B3FEA6404C5C8FFCA3037C95E725725EC89A1F5116F06942942E3D1F6C3F55C6CD23857F2C982543FB7AD69EFE99BCDCD1D96B0D938D39FFDD233284342571A0DFEB6D0D223C91330D1D8C8043DA7EBFA85EC2DA31E07514BEFE51E4C7D6ED79D55355C47821230C18822B07F97211CB9F1AE2B9CC9E9CEB2A76AAE073FCA7974E1744C\"}\n";
		String client = "{\"tag\":\"3E12\",\"signdata\":\"041F6CFE9E482968CE2C706E9AAFA0C58A70BED92C6886DFB60DD2FB2F61A392FC7B47641D017FB80CB6D92D89CC352AB4A8BD77AAC474993374E15F5E16A9A829AA41690CF7CD9D30490CA8B3D4FB61C97AE327AA5794F3AC3848AC7A71A7BFB16467DC8A51E1574260D5A8B88B31D995C660560742A3CF1970B51A8A539D92ACAB5DAEF10929661C1FEDBBB44E3CDFEB4E0D9F0324A86CC72F81C946C183521D6BEF9F16AD135E8262193B44EC1713F5FAAB96D2CD2B41FA98F94A1CDF5E8118EAF645B9DEA8EB4A55BDE0019F52F385B1CCA0B1BBD996765ECCA5DD939684D29A7C7578664F5CD6A404005A45AE71D381169DBE736E922C1C78703F86CD68\",\"ad\":\"AF065436E9FF050CEA3E7A595605B0260BC68AF6AF83D577DB97D005A50A74A6EC836DC5967DA4F6F57CD3248E0368688AEFF656793BF4F6A95281EDD91639C4110BFC9A90695564E3E50A8A1F378E7CFA91C200FFE4766A29ECB795DA1854D8D571C317B6854E36A958A15598EE7D4534C961922D84CF71CB673289F89DBBED1206CB4E93EC8DB164C0EC24BE96BBA3EC07C9A44C02BA30DAF29B6063BAD5C1B2466C8E9209B4A10497D2D4F34A2F3E3A7988B823F0D3FE6A39ED8FF67DB05591073D18C13A72B8B5B1DF5971F968B68BECF74EFFD43C4FA9E0B33EA2E6E627AF4A5BC67FEBB9A532539337A77E211815C6A3C2EC577807C8D59D181A6ABBECFE00E471AE53960DC904F0FB453FA03AA589CF1C5DBAA62E74708F123EF23A1B\"}";
		String serverAuthResult = task.authorize("3E12", client, session);
		System.out.println("serverAuthResult = " + serverAuthResult);

	}

	@Test
	void unreg_test()
	{
		String client = "{\"tag\":\"3E12\",\"id\":\"11C948F98C8D41F42DBB89A25B4B99F43330CAE7CA68CEC621505F0B88F3057F\",\"appid\":\"TEST_ALIAS\",\"type\":\"3\"}";
		String unregist = task.unregist("3E21", client, session);
		System.out.println("unregist = " + unregist);
	}

}
