package interfaceUsuario;

import javax.swing.JButton;

import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon.*;

import dados.Api;
import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
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
		//Image image = ImageIO.read(url);
		//setIconImage(new ImageIcon(image).getImage());
		//BufferedImage image = ImageIO.read(url);
		//ImageIO image = ImageIO.read(url);

		this.addActionListener(this);
		nome = produto;
		preco = valor;
		imagem = link_imagem;
		
		//url = new ImageIcon(this.getClass().getResource("https://ibassets.com.br/ib.item.image.medium/m-44435f9661cd421eaf878e04691a3086.jpeg")); 
		//Image image = ImageIO.read(url);  
		//setIconImage(new ImageIcon(image).getImage());

		try {
			URL url = new URL("https://ibassets.com.br/ib.item.image.medium/m-" + imagem);
			BufferedImage image = ImageIO.read(url);
			
			BufferedImage newImage = new BufferedImage(225, 225, BufferedImage.TYPE_INT_RGB);
			Graphics g = newImage.createGraphics();
			g.drawImage(image, 0, 0, 225, 225, null);
			g.dispose();
			
		    setIcon(new ImageIcon(newImage));
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
		
	}
	
	public void actionPerformed(ActionEvent e) {
		
		//System.out.println("Produto: " + nome + " Preço: " + preco);
		agenteVendas.AtualizarEstoque(nome, (float) (preco));
	}
	
	

}
