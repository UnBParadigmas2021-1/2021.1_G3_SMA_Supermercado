package supermercado;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import jade.core.AID;

class GuiVendaSuper extends JFrame{
	private AgenteVendas agenteVendas;
	
	private JTextField CampoTitulo, CampoPreco;
	
	GuiVendaSuper(AgenteVendas a){
		super(a.getLocalName());
		
		agenteVendas = a;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2,2));
		p.add(new JLabel("Titulo"));
		CampoTitulo = new JTextField(15);
		p.add(CampoTitulo);
		p.add(new JLabel ("Preço:"));
		CampoPreco = new JTextField(15);
		p.add(CampoPreco);
		getContentPane().add(p, BorderLayout.CENTER);
		
		JButton addButton = new JButton("ADD");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				try {
					String title = CampoTitulo.getText().trim();
					String price = CampoPreco.getText().trim();
					agenteVendas.AtualizarEstoque(title, Float.parseFloat(price));
					CampoTitulo.setText("");
					CampoPreco.setText("");
					
					}
				catch(Exception e){
					//JOptionPane.showMessageDialog(GuiVendaSuper.this, "Valor Inválido. " + e.getMessage(), "Error", 1);
					JOptionPane.showMessageDialog(GuiVendaSuper.this, "Valor Incalido. " + e.getMessage());
					
					//JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
					
				}	
				
			}	
		});
		p = new JPanel();
		p.add(addButton);
		getContentPane().add(p, BorderLayout.SOUTH);
		//mata agente
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				agenteVendas.doDelete();
				
			}
			
			
		});
		setResizable(false);
		
		}
		public void showGui() {
			pack();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (int)screenSize.getWidth()/2;
			int y = (int)screenSize.getHeight()/2;
			
			setLocation(x - getWidth()/2, y - getHeight()/2);
			super.setVisible(true);
			
			}
	
}
