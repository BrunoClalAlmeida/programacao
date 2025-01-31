package main;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class ConexaoBanco {

	static String URL = "jdbc:mysql://localhost:3306/loja";
	static String USER = "root";
	static String PASSWORD = "123456";

	public static Connection conexao_com_banco() {
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);

		} catch (SQLException e) {
			System.err.print("ERRO AO CONECTAR NO BANCO DE DADOS" + e.getMessage());
			return null;
		}
	}

	public static void inserirDados() throws SQLException {

		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Digite o nome do produto:");
			String nome = scanner.nextLine();

			System.out.println("Digite o preço do produto:");
			BigDecimal preco = new BigDecimal(scanner.nextLine());

			System.out.println("Digite a quantidade do produto:");
			int quantidade = Integer.parseInt(scanner.nextLine());

			String sql = "INSERT INTO produtos(nome, valor, quantidade) values (?,?,?);";

			try (Connection relizar_conexao = conexao_com_banco();
					PreparedStatement cursor = relizar_conexao.prepareStatement(sql)) {

				cursor.setString(1, nome);
				cursor.setBigDecimal(2, preco);
				cursor.setInt(3, quantidade);

				cursor.executeUpdate();
				System.out.println("Inserido com sucesso ");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void consultarDados() throws SQLException {

		String sql = "Select * from produtos";

		try (Connection relizar_conexao = conexao_com_banco();
				Statement cursor = relizar_conexao.createStatement();
				ResultSet resultado_consulta = cursor.executeQuery(sql)

		) {

			while (resultado_consulta.next()) {
				int id = resultado_consulta.getInt("id");
				String nome = resultado_consulta.getString("nome");
				double preco = resultado_consulta.getDouble("valor");
				int quantidade = resultado_consulta.getInt("quantidade");

				System.out.printf("ID: %d, Nome: %s ,Preço: R$ %.2f , Quantidade:%d%n", id, nome, preco, quantidade);
			}

			System.out.println(resultado_consulta);
		}
	}

	public static void atualizarDados() throws SQLException {

		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Digite o ID do produto a ser atualizado:");
			int id = Integer.parseInt(scanner.nextLine());

			System.out.println("Digite o novo nome do produto:");
			String nome = scanner.nextLine();

			System.out.println("Digite o novo preço do produto:");
			BigDecimal preco = new BigDecimal(scanner.nextLine());

			System.out.println("Digite a nova quantidade do produto:");
			int quantidade = Integer.parseInt(scanner.nextLine());

			String sql = "UPDATE produtos SET nome = ?, valor = ?, quantidade = ? WHERE id = ?";

			try (Connection realizar_conexao = conexao_com_banco();
					PreparedStatement cursor = realizar_conexao.prepareStatement(sql)) {

				cursor.setString(1, nome);
				cursor.setBigDecimal(2, preco);
				cursor.setInt(3, quantidade);
				cursor.setInt(4, id);

				int rowsAffected = cursor.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("Produto atualizado com sucesso.");
				} else {
					System.out.println("Nenhum produto encontrado com o ID fornecido.");
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}

	public static void deletarDados() throws SQLException {

		System.out.println("Produtos disponíveis:");
		consultarDados();

		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Digite o ID do produto que deseja excluir:");
			int id = Integer.parseInt(scanner.nextLine());

			String sql = "DELETE FROM produtos WHERE id = ?";

			try (Connection realizar_conexao = conexao_com_banco();
					PreparedStatement cursor = realizar_conexao.prepareStatement(sql)) {

				cursor.setInt(1, id);

				int rowsAffected = cursor.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("Produto deletado com sucesso.");

				} else {
					System.out.println("Nenhum produto encontrado com o ID fornecido.");
				}
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws SQLException {
		// Connection realizar_conexao = conexao_com_banco();
		// if (realizar_conexao != null) {
		// System.out.println("Conexão estabelecida com Sucesso!");
		// } else {
		// System.out.println("Falha na conexão!");}
		while (true) {
			System.out.println(
					"Bem Vindos, escolha uma opção: \n1 - Inserir Dados \n2 - Consultar Dados \n3 - Atualizar Dados \n4 - Deletar Dados \n5 - Sair ");
			Scanner scanner = new Scanner(System.in);
			int opcao = 0;
			try {
				opcao = Integer.parseInt(scanner.nextLine());

			} catch (Exception e) {
				System.out.println("Digite um numero valido.");
			}

			switch (opcao) {
			case 1:

				inserirDados();
				break;

			case 2:
				consultarDados();
				break;

			case 3:
				atualizarDados();
				break;

			case 4:
				deletarDados();
				break;

			case 5:
				System.out.println("Saindo do Programa!");
				return;
			default:

			}

		}
	}

}