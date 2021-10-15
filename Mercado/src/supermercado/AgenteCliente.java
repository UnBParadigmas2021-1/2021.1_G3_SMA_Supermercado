package supermercado;

//import jade.core.Agent;
//import jade.Boot;

//public class OlaMundo extends Agent{
	//protected void setup() {
		//System.out.println("Ola Mundo");
//	}
//}

//package com.agentes;
import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class AgenteCliente extends Agent{
	
	// Pedido Clente
	private String TituloItem;
	// Lista de Itens
	private AID[] AgenteEstoque; // Agentes Vendedores
	
	@Override
	protected void setup() {
		System.out.println("Olá Cliente " + getAID().getName() + ", o que desejas?");
	
		Object[] args = getArguments();
		if(args != null && args.length > 0) {
			TituloItem = (String) args[0];
			System.out.println("Estou a procura do produto: " + TituloItem + "!");
			
			// adicionando o comportamento
			addBehaviour(new TickerBehaviour(this, 10000) {
				@Override
				protected void onTick() {
					System.out.println("Ola vendedor, quero comprar " + TituloItem);
					//Atualiza a lista de vendedores --- de itens
					DFAgentDescription template
						= new DFAgentDescription();
					ServiceDescription sd
						= new ServiceDescription();
					sd.setType("venda-itens"); // venda-produtos
					template.addServices(sd);
					try {
						DFAgentDescription[] result = DFService.search(myAgent, template);
						System.out.println("Os seguintes vendedores estão disponíveis:");
						AgenteEstoque = new AID[result.length]; //AgentesVendedores
						for (int i = 0; i < result.length;++i) {
							AgenteEstoque[i] = result[i].getName();
							System.out.println(AgenteEstoque[i].getName());
						}
					} catch(FIPAException fe) {
						fe.printStackTrace();
					}
					// Faz pedido
					myAgent.addBehaviour(new PedidoCompra());
			
				}	
			});
		
	}	else {
			System.out.println("Não tem item");
			doDelete();
		}
	}
	
	
	@Override
	protected void takeDown() {
		System.out.println("Agente Cliente " + getAID().getName() + ", valeu! brota mais ae");
		
	}
	
	private class PedidoCompra extends Behaviour{
		private AID produto;
		private float promocional;
		private int repliesCnt = 0;
		private MessageTemplate mt;
		private int step = 0;
		
		@Override
		public void action(){		
			switch(step) {
			case 0:
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				for(int i = 0; i< AgenteEstoque.length;++i) {
					cfp.addReceiver(AgenteEstoque[i]);
				}
				cfp.setContent(TituloItem);
				cfp.setConversationId("comercio-itens"); //comercio produtos
				cfp.setReplyWith("cfp"+ System.currentTimeMillis());
				myAgent.send(cfp);
				//Prepara o modelo de recever as respostas
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("comercio-itens"), 
						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				step = 1;
				break;
			case 1:
				ACLMessage reply = myAgent.receive(mt);
				if(reply != null) {
					if(reply.getPerformative() == ACLMessage.PROPOSE) {
						float preco = Float.parseFloat(reply.getContent()) ;
						if(produto == null || preco < promocional) {
							promocional = preco;
							produto = reply.getSender();
						}
					
					}
					repliesCnt++;
					if(repliesCnt >= AgenteEstoque.length) {
						step = 2;
					}
				}
				else {
						block();
					}
					break;
				case 2:
						//Envio de pedido de comprar 
						ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
						order.addReceiver(produto);
						order.setContent(TituloItem);
						order.setConversationId("comercio-itens");
						order.setReplyWith("Orden" + System.currentTimeMillis());
						myAgent.send(order);
						
						// modelo para resposta de ordem de compra
						mt = MessageTemplate.and(MessageTemplate.MatchConversationId("comercio-itens"),
								MessageTemplate.MatchInReplyTo(order.getReplyWith()));
						step = 3;
						break;
					
				case 3:
					// Recebe a resposta
					reply = myAgent.receive(mt);
					if(reply != null) {
						// Resposta da ordem de compra
						if(reply.getPerformative() == ACLMessage.INFORM) {
							//compra bem sucedida
							System.out.println("Produto: "+ TituloItem + " comprado de " + reply.getSender().getName());
							System.out.println("Preço: R$ " + promocional);
							myAgent.doDelete();
						} else {
							System.out.println("Desculpe mas o item ja foi vendido");
						}
						step=4;
						
					} else {
						block();
					}
					break;
						
			
					}
				}
		@Override
		public boolean done() {
			if (step == 2 && produto == null) {
				System.out.println("Desculpe mas o produto " + TituloItem + " mas não está a venda");
			}
			return ((step ==2 && produto == null || step ==4));
		}
				
			}// fim
	}
			

	
