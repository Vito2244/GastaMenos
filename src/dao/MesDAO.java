package dao;

import model.Gastos;
import model.Mes;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MesDAO {

    public void salvar(Mes mes) throws Exception {
        Connection conn = DBConnection.getConnection();
        String sqlGastos = "INSERT INTO gastos (casa, seguros, carro, educacao, internet, saude, cartao, contaAgua, contaLuz, outros) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmtGastos = conn.prepareStatement(sqlGastos, Statement.RETURN_GENERATED_KEYS);
        Gastos g = mes.gastos;
        stmtGastos.setDouble(1, g.casa);
        stmtGastos.setDouble(2, g.seguros);
        stmtGastos.setDouble(3, g.carro);
        stmtGastos.setDouble(4, g.educacao);
        stmtGastos.setDouble(5, g.internet);
        stmtGastos.setDouble(6, g.saude);
        stmtGastos.setDouble(7, g.cartao);
        stmtGastos.setDouble(8, g.contaAgua);
        stmtGastos.setDouble(9, g.contaLuz);
        stmtGastos.setDouble(10, g.outros);
        stmtGastos.executeUpdate();
        ResultSet rs = stmtGastos.getGeneratedKeys();
        rs.next();
        int gastosId = rs.getInt(1);

        String sqlMes = "INSERT INTO mes (data, gastos_id, salario) VALUES (?, ?, ?)";
        PreparedStatement stmtMes = conn.prepareStatement(sqlMes);
        stmtMes.setDate(1, Date.valueOf(mes.data));
        stmtMes.setInt(2, gastosId);
        stmtMes.setDouble(3, mes.salario);
        stmtMes.executeUpdate();
    }

    public List<Mes> listarTodos() throws Exception {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT m.id AS mes_id, m.data, m.salario, g.id AS gastos_id, g.* " +
                "FROM mes m JOIN gastos g ON m.gastos_id = g.id ORDER BY m.data DESC";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        List<Mes> lista = new ArrayList<>();
        while (rs.next()) {
            Gastos g = new Gastos();
            g.id = rs.getInt("id");
            g.casa = rs.getDouble("casa");
            g.seguros = rs.getDouble("seguros");
            g.carro = rs.getDouble("carro");
            g.educacao = rs.getDouble("educacao");
            g.internet = rs.getDouble("internet");
            g.saude = rs.getDouble("saude");
            g.cartao = rs.getDouble("cartao");
            g.contaAgua = rs.getDouble("contaAgua");
            g.contaLuz = rs.getDouble("contaLuz");
            g.outros = rs.getDouble("outros");


            Mes mes = new Mes();
            mes.id = rs.getInt("id");
            mes.data = rs.getDate("data").toLocalDate();
            mes.salario = rs.getDouble("salario");
            mes.gastos = g;
            lista.add(mes);
        }
        return lista;
    }
}
