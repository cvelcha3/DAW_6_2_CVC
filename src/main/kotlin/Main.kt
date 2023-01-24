import java.util.concurrent.TimeUnit
import kotlin.math.ceil

fun main() {
    val numeroTiros = 9
    val beretta = Pistola("beretta 1911", 5, 7, "9mm", 5, "Pequeño")
    val galil = Rifle("galil 16", 23, 30, "15mm", 15, "Amplio")
    val rpg = Bazooka("RPG-9", 4, 6, "explosiva", 150, "Amplio")
    val casoplon = Casa("villagrande",5,10,"ladrillo",300,"Amplio")
    val cochazo = Coche("kia picanto",7,16,"caballo",500,"Amplio")
    val bocata = Bocadillo("EL bocata",5,6,"aceite",1,"Pequeño")
    val listaDeArmas = listOf<FireArm>(beretta,galil,rpg,casoplon,cochazo,bocata)

    for (i in 0..numeroTiros){
        listaDeArmas.random().dispara(1)
    }
    /*
    multiplicadorBalas
    beretta*1
    galil*2
    rpg*3
    casoplon*1
    cochazo*4
    bocata*3
    */

}

abstract class FireArm(
    private var nombre: String,
    private var municion: Int,
    private val capacidad: Int,
    private val tipoDeMunicion: String,
    private val danio: Int,
    private var radio: String): Disparable {

    protected var multiplicadorBalas=1  //Al declarar el multiplicador, el temporizador y el sonido, son fácilmente modificables por subclases
    protected var miniTimer=500L        //antes de llamar a los métodos.
    protected var firingSound="bam"
    init {

        if (municion>capacidad)
            municion=capacidad

        if (radio !in listOf<String>("Pequeño","Amplio"))
            radio="Pequeño"

    }
    override fun dispara(municionARestar: Int) {
        val asteriskBar = "****************************"
        println(asteriskBar)
        println(nombre)
        println(asteriskBar)
        if ((municion > 0) and ((municionARestar*multiplicadorBalas) <= municion)) {
            val numDisparosDecimal: Float = ((municionARestar.toFloat())/(multiplicadorBalas.toFloat()))
            val numDisparos = ceil(numDisparosDecimal).toInt()
            for (i in 1..numDisparos) {
                TimeUnit.MILLISECONDS.sleep(miniTimer)
                println(firingSound)
            }
            municion-=municionARestar*multiplicadorBalas
        }
        else{
            println("No hay balas suficientes. Balas necesarias por tiro: $multiplicadorBalas")
        }
        val danioTotal= danio*municionARestar
        println(asteriskBar)
        println("$nombre: $municion/$capacidad balas de $tipoDeMunicion / daño: ($danio)x$municionARestar=$danioTotal")
        println(asteriskBar)
    }

    override fun recarga(municionARecargar: Int){   //Quería probar a añadir un valor predeterminado al parámetro pero no veo la forma de implementarlo con interfaz
        if ((municionARecargar+municion)<=capacidad){
            municion+=municionARecargar
            println("Recarga completada. ($municion/$capacidad)")
        }
        else{
            println("Recarga fallida.")
        }
    }
}

class Pistola(
    nombre: String,
    municion: Int,
    capacidad: Int,
    tipoDeMunicion: String,
    danio: Int,
    radio: String):FireArm(nombre, municion, capacidad, tipoDeMunicion, danio, radio) {
    override fun dispara(municionARestar: Int) {
        super.dispara(municionARestar)
    }

    override fun recarga(municionARecargar: Int) {
        super.recarga(municionARecargar)
    }
}

class Rifle(nombre: String,
            municion: Int,
            capacidad: Int,
            tipoDeMunicion: String,
            danio: Int,
            radio: String):FireArm(nombre, municion, capacidad, tipoDeMunicion, danio, radio) {
    override fun dispara(municionARestar: Int) {
        super.miniTimer=250L
        super.multiplicadorBalas=2
        super.dispara(municionARestar)
    }

    override fun recarga(municionARecargar: Int) {
        super.recarga(municionARecargar)
    }
}

class Bazooka(nombre: String,
              municion: Int,
              capacidad: Int,
              tipoDeMunicion: String,
              danio: Int,
              radio: String):FireArm(nombre, municion, capacidad, tipoDeMunicion, danio, radio) {
    override fun dispara(municionARestar: Int, ) {
        super.miniTimer=1000L
        super.multiplicadorBalas=3
        super.firingSound="BOOM"
        super.dispara(municionARestar)

    }

    override fun recarga(municionARecargar: Int) {
        super.recarga(municionARecargar)
    }
}

class Casa(nombre: String,
           municion: Int,
           capacidad: Int,
           tipoDeMunicion: String,
           danio: Int,
           radio: String):FireArm(nombre, municion, capacidad, tipoDeMunicion, danio, radio) {
    override fun dispara(municionARestar: Int, ) {
        super.miniTimer=2000L
        super.multiplicadorBalas=1
        super.firingSound="??"
        super.dispara(municionARestar)

    }

    override fun recarga(municionARecargar: Int) {
        super.recarga(municionARecargar)
    }
}

class Coche(nombre: String,
            municion: Int,
            capacidad: Int,
            tipoDeMunicion: String,
            danio: Int,
            radio: String):FireArm(nombre, municion, capacidad, tipoDeMunicion, danio, radio) {
    override fun dispara(municionARestar: Int, ) {
        super.miniTimer=750L
        super.multiplicadorBalas=4
        super.firingSound="broom broom"
        super.dispara(municionARestar)

    }

    override fun recarga(municionARecargar: Int) {
        super.recarga(municionARecargar)
    }
}

class Bocadillo(nombre: String,
                municion: Int,
                capacidad: Int,
                tipoDeMunicion: String,
                danio: Int,
                radio: String):FireArm(nombre, municion, capacidad, tipoDeMunicion, danio, radio) {
    override fun dispara(municionARestar: Int, ) {
        super.miniTimer=1000L
        super.multiplicadorBalas=3
        super.firingSound="ñam"
        println("que empape")
        super.dispara(municionARestar)

    }

    override fun recarga(municionARecargar: Int) {
        super.recarga(municionARecargar)
    }
}

interface Disparable {
    fun dispara(municionARestar: Int)
    fun recarga(municionARecargar: Int)
}

/*
Los beneficios obtenidos de una clase abstracta son que al no poder instanciarse, sirven para dar estructura y que las
subclases presentes hereden los constructores, sin tener de preocuparnos de que se utilizen indebidamente.

Por otro lado, la interfaz obliga a implementar las funciones, asegurando de manera indirecta que están presentes
todos los métodos necesarios para las clases que heredan esas interfaces.

También, aunque no sea obvio porque es redundande, todas las clases sin modificador escrito son finales, es decir,
que no se puede heredar de ellas.
 */


