package br.com.astarlabs.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import br.com.astarlabs.client.ApiException;
import br.com.astarlabs.client.api.SearchApi;
import br.com.astarlabs.client.api.SendApi;
import br.com.astarlabs.client.model.BlockchainNetwork;
import br.com.astarlabs.client.model.SingleResult;
import br.com.astarlabs.client.model.Transaction;

public class Blockchain {

	private String token;
	private Integer account;
	private String user;
	private String pass;

	/**
	 * Registro um hash (bytearray) a partir de um arquivo
	 * @param file Arquivo que sera registrado na BLockchain
	 * @param blockchainNetwork qual a rede blockchain o registro deverá ser feito
	 * @param testMode indica se o registro deve ser feito na rede de teste ou na rede principal
	 * @return Texto de numero inteiro contentdo o ID da transação validada
	 * @throws IOException
	 * @throws ApiException
	 */
	@Deprecated
	public String registrarDocumento(File file, final BlockchainNetwork blockchainNetwork, final Integer testMode) throws IOException, ApiException { 
		return registrarDocumento(Files.readAllBytes(file.toPath()), blockchainNetwork, testMode); 
	}

	/**
	 * Registro um hash (bytearray) a partir de um arquivo
	 * @param file Arquivo que sera registrado na BLockchain
	 * @param blockchainNetwork qual a rede blockchain o registro deverá ser feito
	 * @param testMode indica se o registro deve ser feito na rede de teste ou na rede principal
	 * @return Texto de numero inteiro contentdo o ID da transação validada
	 * @throws IOException
	 * @throws ApiException
	 */
	public String registrarDocumento(File file, final BlockchainNetwork blockchainNetwork, final Integer testMode, final Boolean isForce) throws IOException, ApiException {
		return registrarDocumento(Files.readAllBytes(file.toPath()), blockchainNetwork, testMode, isForce);
	}

	@Deprecated
	public String registrarDocumento(String fileString, final BlockchainNetwork blockchainNetwork, final Integer testMode) throws ApiException { 
	    return registrarDocumento(fileString.getBytes(), blockchainNetwork, testMode); 
	}
	
	public String registrarDocumento(String fileString, final BlockchainNetwork blockchainNetwork, final Integer testMode, final Boolean isForce) throws ApiException {
		return registrarDocumento(fileString.getBytes(), blockchainNetwork, testMode, isForce);
	}

	@Deprecated
	public String registrarDocumento(byte[] bytesFile, final BlockchainNetwork blockchainNetwork, final Integer testMode) throws ApiException {
		String hash = DoubleSha256.hashFile(bytesFile);

		return registrarConteudo(hash, blockchainNetwork, testMode);
	}
	
	public String registrarDocumento(byte[] bytesFile, final BlockchainNetwork blockchainNetwork, final Integer testMode, final Boolean isForce) throws ApiException {
		String hash = DoubleSha256.hashFile(bytesFile);

		return registrarConteudo(hash, blockchainNetwork, testMode, isForce);
	}

	@Deprecated
	public String registrarConteudo(String string, final BlockchainNetwork blockchainNetwork, final Integer testMode) throws ApiException {
		String hash = string;
		token = getMyToken();

		SendApi api = new SendApi();
		SingleResult singleResult = api.sendHash(token, account, user, pass, hash, blockchainNetwork, testMode);

		if (singleResult.getResult() != null && singleResult.getStatus()) {
			return singleResult.getResult();
		}

		return null;
	}
	
	public String registrarConteudo(String string, final BlockchainNetwork blockchainNetwork, final Integer testMode, final Boolean isForce) throws ApiException {
		String hash = string;
		token = getMyToken();

		SendApi api = new SendApi();
		SingleResult singleResult = api.sendHash(token, account, user, pass, hash, blockchainNetwork, testMode, isForce);

		if (singleResult.getResult() != null && singleResult.getStatus()) {
			return singleResult.getResult();
		}

		return null;
	}

	/**
	 * 
	 * @param bytesFile
	 * @return
	 * @throws ApiException
	 */
	public Transaction consultarDocumentoPorConteudo(byte[] bytesFile) throws ApiException {
		token = getMyToken();

		SearchApi api = new SearchApi();
		List<Transaction> transactions = api.searchByContent(token, account, user, pass, new String(bytesFile));

		if (transactions != null && transactions.size() > 0) {
			return transactions.get(0);
		}

		return null;	
	}	

	public Transaction validarRegistroDocumento(Integer id) throws ApiException {
		token = getMyToken();

		Transaction transaction = new Transaction();
		SearchApi sa = new SearchApi();	
		transaction = sa.searchByAPIID(token, account, user, pass, id);

		return transaction;
	}

	private String getMyToken() {
		try {
			this.token = Token.sign(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	} 

	public Blockchain(String token, Integer account, 
			String user, String pass) {
		super();
		this.token = token;
		this.account = account;
		this.user = user;
		this.pass = pass;
	}

}