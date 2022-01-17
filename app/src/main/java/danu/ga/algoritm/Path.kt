package danu.ga.algoritm

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Path:Comparable<Path>  {
    private lateinit var path:Array<City?>
    private var numCities:Int ?= 0
    private var fitness:Int ?= 0
    private var cost = 0.0


    constructor(numcities: Int){
        //viewModel database
        this.numCities = numcities
        createPath()
        cost = 0.0
        calculateCost()
        fitness = 0
    }

    //menghitung harga bayar
    fun calculateCost() {
        this.cost = 0.0
        var i = 0
        while (i < numCities!! - 1) {
            this.cost += path[i]!!.distance(path[i + 1]?.getLat()!!.toDouble(), path[i + 1]?.getLng()!!.toDouble())
            i++
        }
        this.cost += path[path.size - 1]!!.distance(path[0]?.getLat()!!.toDouble(), path[0]?.getLng()!!.toDouble())
    }

    //Init Path Wisata
    fun createPath(){
       this.path= Array(numCities!!){ City(1, "",0.0, 0.0,0,0) }

        //data
        var path2 :Array<City> = Array(14){ City(1, "",0.0, 0.0,0,0) }
//        path2[0] = City(1,-6.833750768927492,108.23349724087834,6,20)
//        path2[1] = City(2,-6.837117742309791,108.23474243750054,8,15)
//        path2[2] = City(3,-6.835167279563472,108.22778017155827,6,21)
//        path2[3] = City(4,-6.83291815821884,108.2148901562182,7,18)


//        //data baru
        path2[0]= City(1 , "Curug Padjajaran/Bintang Padjajaran",-7.141948728625862, 107.42001699715476, 7,15)
        path2[1]= City(2, "Curug Awi Langit",-7.13786837250741, 107.4003322683194, 9, 15)
        path2[2]= City(3, "Curug Meong",-7.147259667366287, 107.43302599715466, 9, 15)
        path2[3]= City(4, "Curug Cigadong",-7.140309854149551, 107.40050826831946, 7, 15)
        path2[4]= City(5, "Latihan Pencak Silat",-7.134110954021378, 107.42146499715453, 14, 17)
        path2[5]= City(6, "Jeruk Dekopon dan Buah Tin Kebun Haji Iin",-7.1263228460283345, 107.4213933529785, 8, 10)
        path2[6]= City(7, "Wortel, bawang, kol",-7.131829661631447, 107.42214335297857, 6, 8)
        path2[7]= City(8, "Bawang daun, labusiam, wortel, strawberry",-7.134542142316982, 107.42175399715457, 6, 8)
        path2[8]= City(9, "Pinus Land",-7.162362963217781, 107.43179799715483, 11, 17)
        path2[9]= City(10, "Bird Watching",-7.158514509943944, 107.41803662414344, 11, 17)
        path2[10]= City (11, "Bunga Potong",-7.133991370886841, 107.42228635297856, 8, 10)
        path2[11]= City(12, "Karawitan dan Wayang Bodor",-7.127602305985058, 107.43041099715458, 10, 15)
        path2[12]= City(13, "Reog",-7.127168138379776, 107.43032926831937, 10, 15)
        path2[13]= City( 14, "Lampion, lukisan dari bambu",-7.130162723424981, 107.43236826831934, 14, 17)



        val currentTimestamp = System.currentTimeMillis()
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH.mm")
        val formatted = current.format(formatter)
        val time:Double = formatted.toDouble()

        //memasukan ke array utama
        var list:MutableList<City> = mutableListOf()
        var i = 0
        while (i < path2.size ){
                if (path2[i].tutup!! > time && path2[i].buka!! < time){
                    list.add(City(path2[i].id!!,path2[i].nama!!,path2[i].lat!!,path2[i].lng!!,path2[i].buka!!,path2[i].tutup!!))
                }
            i++
        }
        path = list.toTypedArray()
//        path = arrayOfNulls(numCities!!)
//        for (i in 0 until path.size) {
//            path[i] = City(i, RandomNum(1, 99).toDouble(), RandomNum(1, 99).toDouble(),0,0)
//        }

    }
    fun RandomNum(min: Int, max: Int): Int {
        return min + Random().nextInt(max - min)
    }
    fun RandomTime(min: Int, max: Int):Int{
        return  min + Random().nextInt(max - min)
    }

    fun getPath(): Array<City?> {
        return this.path
    }

    fun setPath(path: Array<City?>) {
        this.path = path
        calculateCost()
    }


    fun getCost(): Double? {
        return cost
    }

    fun setCost(distance: Double) {
        this.cost = distance
    }


    //Fitness
    fun getFitness(): Int? {
        return fitness
    }

    fun setFitness(fitness: Int) {
        this.fitness = fitness
    }

    override fun compareTo(obj: Path): Int {
        val tmp = obj
        return if (cost!! < tmp.cost!!) {
            1
        } else if (cost!! > tmp.cost!!) {
            -1
        } else {
            0
        }
    }


}