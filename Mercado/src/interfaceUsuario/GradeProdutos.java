package interfaceUsuario;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import dados.Api;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import dados.Api;
import jade.core.AID;
import supermercado.AgenteVendas;

public class GradeProdutos extends JButton implements ActionListener{
	//ImageIcon teste1,teste2;
	byte value=0;
	float preco;
	String nome, imagem;
	private AgenteVendas agenteVendas;
	
	public GradeProdutos(AgenteVendas a) {
		super(a.getLocalName());
		agenteVendas = a;


	}
	public void XOButtonInsert(String produto, float valor, String link_imagem) {

	//	agenteVendas = new AgenteVendas();
		//X=new ImageIcon(this.getClass().getResource("image.png"));
		//O=new ImageIcon(this.getClass().getResource("image2.png"));
		this.addActionListener(this);
		nome = produto;
		preco = valor;
		imagem = link_imagem;
		setText(nome);

	}
	
	public void actionPerformed(ActionEvent e) {
		//setText(nome);
		//agenteVendas.run();
		/*value++;
		value%=3;
		switch(value) {
		case 0:
			setIcon(null);
			break;
		case 1:
			//setIcon(nome);
			setText(nome);
			break;
		case 2:
			//setIcon(O);
			setText(nome);
			break;
		}*/
		System.out.println("Produto: " + nome + " Preço: " + preco);
		agenteVendas.AtualizarEstoque(nome, (float) (preco));
	}
	
	

}
