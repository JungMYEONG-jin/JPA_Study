package kakao.getCI.apple;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import sun.security.ec.ECPrivateKeyImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.interfaces.ECPrivateKey;
import java.util.Date;

@Service
public class AppleService {

    public static final String issuer_Id = "?";
    public static final String keyId = "FXNMXR3KNH";
    public static final String keyPath = "static/apple/AuthKey_FXNMXR3KNH.p8";


    public String getAppInfos(String jwt) throws MalformedURLException {
        System.out.println("jwt = " + jwt);
        String result = "";
        URL url = new URL("https://api.appstoreconnect.apple.com/v1/apps");

        try{
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer "+jwt);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            String line = "";
            String res = "";
            while((line=br.readLine())!=null)
            {
                res+=line;
            }

            System.out.println("res = " + res);
            result = res;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }




    public String createJWT( )
    {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(keyId).type(JOSEObjectType.JWT).build();
        Date now = new Date();

        JWTClaimsSet claimsSet = new JWTClaimsSet();
        claimsSet.setIssuer(issuer_Id);
        claimsSet.setIssueTime(now);
        claimsSet.setExpirationTime(new Date(now.getTime()+3600000));
        claimsSet.setAudience("appstoreconnect-v1");


        SignedJWT jwt = new SignedJWT(header,claimsSet);

        try{
            ECPrivateKey ecPrivateKey = new ECPrivateKeyImpl(readPrivateKey(keyPath));
            JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey.getS());
            jwt.sign(jwsSigner);

        }catch(InvalidKeyException e)
        {
            e.printStackTrace();
        }catch (JOSEException e)
        {
            e.printStackTrace();
        }

        return jwt.serialize();

    }

    private byte[] readPrivateKey(String keyPath)
    {
        Resource resource = new ClassPathResource(keyPath);
        byte[] content = null;

        try(FileReader keyReader = new FileReader(resource.getFile());
            PemReader pemReader = new PemReader(keyReader))
        {
            PemObject pemObject = pemReader.readPemObject();
            content = pemObject.getContent();

        }catch(IOException e)
        {

        }
        return content;
    }
}