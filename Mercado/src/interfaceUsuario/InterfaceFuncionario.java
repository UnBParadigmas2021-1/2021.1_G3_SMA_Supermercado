package interfaceUsuario;

/*
//package teste;

//import java.util.Scanner;
//import dados.Api;
//public class Testes {

  //  public static void main(String[] args) {
    //	System.out.println("Oi");
    // Api api = new Api();
    	api.get();
    //}
//}
    	*/

import javax.swing.JPanel;

import java.awt.GridLayout;
import supermercado.AgenteVendas;
import javax.swing.JFrame;
import dados.Api;
import jade.util.leap.ArrayList;  


public class InterfaceFuncionario extends JFrame{
	private AgenteVendas agenteVendas;

	JPanel p = new JPanel();
	GradeProdutos buttons[] = new GradeProdutos[30];
	Api instanceApi = new Api();
	
	
	
	 public InterfaceFuncionario(AgenteVendas a) {
		super(a.getLocalName());

		agenteVendas = a;

		//super("Testes");
		setSize(800,600);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		p.setLayout(new GridLayout(3,3));
		
		
		ArrayList lista_itens = (ArrayList) instanceApi.get();
		//String ok = lista_itens[1];
		
		for (int i = 0; i < lista_itens.size() -3;i+=3) {
			//System.out.println(lista_itens.get(i));
			//System.out.println(lista_itens.get(i+1));
			//System.out.println(lista_itens.get(i+2));
			GradeProdutos xo = new GradeProdutos(a);
			xo = new GradeProdutos(a);
			
			buttons[i] = xo;
			xo.XOButtonInsert((String) lista_itens.get(i), (Float) lista_itens.get(i+1), (String) lista_itens.get(i+2));
			//agenteVendas.AtualizarEstoque("Naruto", (float) (1.5 * i));
			p.add(buttons[i]);
		}
		
		add(p);
		setVisible(true);
	}
	
}

