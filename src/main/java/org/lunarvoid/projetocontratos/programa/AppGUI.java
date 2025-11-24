package org.lunarvoid.projetocontratos.programa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.lunarvoid.projetocontratos.entidades.Contrato;
import org.lunarvoid.projetocontratos.entidades.Parcela;
import org.lunarvoid.projetocontratos.repositores.ContratoRepositor;
import org.lunarvoid.projetocontratos.repositores.ParcelaRepositor;

public class AppGUI {

    private JFrame frame;
    private JTextField numeroField, valorField, qtdField;
    private DefaultTableModel tableModel;
    private JTable table;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppGUI().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Sistema de Contratos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        // Painel principal com padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(criarInputPanel(), BorderLayout.NORTH);
        mainPanel.add(criarTabelaParcelas(), BorderLayout.CENTER);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel criarInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titulo = new JLabel("Cadastro de Contrato");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(new Color(34, 49, 63));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        gbc.gridwidth = 1;

        // Número do contrato
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Número do Contrato:"), gbc);
        numeroField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(numeroField, gbc);

        // Valor do contrato
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Valor do Contrato:"), gbc);
        valorField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(valorField, gbc);

        // Quantidade de parcelas
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Quantidade de Parcelas:"), gbc);
        qtdField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(qtdField, gbc);

        // Botão salvar
        JButton salvarBtn = new JButton("Salvar Contrato");
        salvarBtn.setBackground(new Color(41, 128, 185));
        salvarBtn.setForeground(Color.WHITE);
        salvarBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        salvarBtn.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(salvarBtn, gbc);

        salvarBtn.addActionListener(e -> salvarContrato());

        return panel;
    }

    private JScrollPane criarTabelaParcelas() {
        String[] colunas = {"Data Parcela", "Valor", "Status"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // impede edição direta
            }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(41, 128, 185)), "Parcelas", 0, 0, new Font("SansSerif", Font.BOLD, 14), new Color(41, 128, 185)));
        return scroll;
    }

    private void salvarContrato() {
        try {
            String numero = numeroField.getText();
            Double valor = Double.parseDouble(valorField.getText());
            int qtd = Integer.parseInt(qtdField.getText());

            Contrato ct = new Contrato(numero, LocalDate.now(), valor);
            new ContratoRepositor().addContrato(ct, qtd);

            List<Parcela> parcelas = new ParcelaRepositor().getParcelasBanco(ct);

            tableModel.setRowCount(0);
            for (Parcela p : parcelas) {
                tableModel.addRow(new Object[]{
                        p.getMes(),
                        p.getValorParcela(),
                        p.getStatus()
                });
            }

            JOptionPane.showMessageDialog(frame, "Contrato salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
