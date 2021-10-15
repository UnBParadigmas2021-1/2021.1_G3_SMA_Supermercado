package supermercado;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Hashtable;


public class AgenteVendas extends Agent{
 // guarda os itens
 private Hashtable estoque; //catalago
 //permite adicionar itens
 private GuiVendaSuper aba;
 
 //incinaliza agente
 @Override
 protected void setup() {
  //cria catálago
  System.out.println("Bem vindo " + getAID().getName() + " tenha boas vendas");
  estoque = new Hashtable();
  
  aba = new GuiVendaSuper(this);
  aba.showGui();
  
  // registra o serviço do vendedor de livros
  DFAgentDescription dfd = new DFAgentDescription();
  dfd.setName(getAID());
  ServiceDescription sd = new ServiceDescription();
  sd.setType("venda-itens");
  sd.setName("Carteira JADE");
  dfd.addServices(sd);
  try {
   DFService.register(this, dfd);
   
  }
  catch (FIPAException fe){
   fe.printStackTrace();
   
  }
  // addo comportamento para servir as consultas dos compradores
  addBehaviour(new DemandaOfertaServidor());
  
  // add o comportamento as compras dos compradores
  addBehaviour(new OrdenemCompraServidor());
  
 }
 
 //finaliza a missao do agente
 @Override
 protected void takeDown(){
  try {
   DFService.deregister(this);
  }
  catch(FIPAException fe) {
   fe.printStackTrace();
  }
  aba.dispose();
  // demite o agente
  System.out.println("Agente Vendedor "+getAID().getName() + " sua missao kbo!!");
 }
 
 public void AtualizarEstoque(final String titulo, Float precio) {
  addBehaviour(new OneShotBehaviour() {
   
   public void action() {
    estoque.put(titulo, new Float(precio));
    System.out.println("Livro : "+ titulo + " esta disponivel pelo valor de " + precio);

   }
   });
 }
 
 private class DemandaOfertaServidor extends CyclicBehaviour{
  @Override
  public void action() {
   MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
   ACLMessage msg = myAgent.receive(mt);
   if(msg != null) {
    // processar a mensagem
    String titulo = msg.getContent();
    ACLMessage resposta = msg.createReply();
    
    Float valor = (Float) estoque.get(titulo);
    if(valor != null) {
     // se o produto estiver disṕonivel responde com o valor
     resposta.setPerformative(ACLMessage.PROPOSE);
     resposta.setContent(String.valueOf(valor.intValue()));
    
     
    }else {
     resposta.setPerformative(ACLMessage.REFUSE);
     resposta.setContent("Não tem");
     
    }
    myAgent.send(resposta);
   
  }else {
   block();
  }
 }
}// Fim classe
 
 private class OrdenemCompraServidor extends CyclicBehaviour{
  @Override
  public void action() {
   
   
   MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
   ACLMessage msg = myAgent.receive(mt);
   if(msg != null) {
    //processa a mensagem
    String titulo = msg.getContent();
    ACLMessage resposta = msg.createReply();
    
    Float valor = (Float) estoque.remove(titulo);
    
    if(valor != null) {
     resposta.setPerformative(ACLMessage.INFORM);
     System.out.println("O livre " + titulo + " foi vendido ao agente " + msg.getSender().getName());
     
    }else {
     // o item vendido
     resposta.setPerformative(ACLMessage.FAILURE);
     resposta.setContent("nao-disponivel");
     
    }
    myAgent.send(resposta);
    }else {
     block();
     }
  }
 }// Fim classe
  
  
  
}