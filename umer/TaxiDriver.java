
/**
 * Write a description of class TaxiDrive here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.time.LocalDate;

public class TaxiDriver extends Driver
{
    // instance variables - replace the example below with your own
    private String empresa;
    private String carroDeEmpresa;
    // boolean para dizer se esta dentro do horario de trabalho da empresa
    private boolean dispEmpresa;

    public TaxiDriver()
    {
        empresa = "";
        carroDeEmpresa = "";
        dispEmpresa = false;
    }
    public TaxiDriver(String email,String nome, String password, String morada, LocalDate data,
                      boolean disponivel,String carro,
                      String empresa, String carroDeEmpresa, boolean dispEmpresa){
        super(email,nome,password,morada,data,disponivel,carro);
        this.empresa = empresa;
        this.carroDeEmpresa = carroDeEmpresa;
        this.dispEmpresa = dispEmpresa;
    }
    public TaxiDriver( Driver d, String empresa, String carroDeEmpresa, boolean dispEmpresa){
        super(d);
        this.empresa = empresa;
        this.carroDeEmpresa = carroDeEmpresa;
        this.dispEmpresa = dispEmpresa;
    }
    public TaxiDriver(TaxiDriver t){
        super(t);
        empresa = t.getEmpresa();
        carroDeEmpresa = t.getCarroDeEmpresa();
        dispEmpresa = t.getDispEmpresa();
    }
    
    public String getEmpresa(){
        return empresa;
    }
    public String getCarroDeEmpresa(){
        return carroDeEmpresa;
    }
    public boolean getDispEmpresa(){
        return dispEmpresa;
    }
     public String getVeiculo(){
        if( ! dispEmpresa )
            return super.getVeiculo();
        else
            return carroDeEmpresa;
    }
    
    public void setEmpresa( String empresa){
        this.empresa = empresa;
    }
    public void setCarroDeEmpresa(String carro){
        carroDeEmpresa = carro;
    }
    public void setDispEmpresa(boolean flag, DataBase db){
        
        
        Company c = db.getEmpresa( empresa);
        
        if( flag ){
            //procura um carro
            try{
                carroDeEmpresa =  c.disponibilizaCarro() ;
                this.dispEmpresa = true;
                setDisponivel(true);
            }
            catch( CarroNaoExistenteException e){
                System.out.println("Neste momento a empresa não tem carro disponivel para lhe atribuir");
            }
        }else{
            //liberta o carro
            c.devolveCarro( carroDeEmpresa);
            this.dispEmpresa  = false;
        }
    }
    
    public TaxiDriver clone(){
        return new TaxiDriver(this);
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        sb.append( super.toString());
        sb.append("\nEmpresa:");
        sb.append( empresa +"\n" );
        sb.append("Esta a trabalhar para a empresa: ");
        sb.append(dispEmpresa + "\n");
        if( dispEmpresa){
            sb.append("Carro de empresa: ");
            sb.append( carroDeEmpresa + "\n");
        }
        return sb.toString();
    }
}


