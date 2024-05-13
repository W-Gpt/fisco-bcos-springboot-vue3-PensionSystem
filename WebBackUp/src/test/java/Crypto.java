import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.signature.ECDSASignatureResult;
import org.fisco.bcos.sdk.crypto.signature.SignatureResult;
import org.fisco.bcos.sdk.model.CryptoType;
import org.junit.jupiter.api.Test;
//import org.junit.Test;


public class Crypto {
   final String privateKey = "97a9bed09438bb51e1c09a836dfbecae6d17bd6a7057d21ffeaa03c53bb1ec47";
   final String data = "FISCO BCOS Signature";

   @Test
   public void test(){
       CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);//************
       // 使用privateKey创建密钥对
       CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair();//************
       ECDSASignatureResult ecdsaSignatureResult = generateSigantureWithSecp256k1(data,cryptoSuite);
       Boolean result = verifySignature(ecdsaSignatureResult,cryptoSuite,data);
       System.out.println("ECDSASignatureResult: "+ecdsaSignatureResult.convertToString());
       System.out.println("Verifying signatures： "+result);
   }

   public ECDSASignatureResult generateSigantureWithSecp256k1(String data,CryptoSuite cryptoSuite)
   {
       // 计算传入数据的哈希(keccak256哈希算法)
       String hashData = cryptoSuite.hash(data); //************
       // 生成签名
       ECDSASignatureResult ecdsaSignatureResult = (ECDSASignatureResult) cryptoSuite.sign(hashData,cryptoSuite.getCryptoKeyPair());//************
       return ecdsaSignatureResult;
   }

   // 验证secp256k1签名（入参为String）
   public boolean verifySignature(SignatureResult signatureResult, CryptoSuite cryptoSuite, String data)
   {
       // 计算data的哈希(keccak256k1哈希算法)
       String hashData = cryptoSuite.sign(data,cryptoSuite.getCryptoKeyPair()).convertToString();//************
       // 验证签名
       boolean result = cryptoSuite.verify(cryptoSuite.getCryptoKeyPair().getHexPublicKey(),hashData,signatureResult.convertToString()) ;//************
       return result;
   }
}
