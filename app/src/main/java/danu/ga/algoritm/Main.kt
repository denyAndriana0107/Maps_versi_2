package danu.ga.algoritm

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class Main {
}
fun main(){
    val GAUSE = true
    var numberOfGenerations = 4
    var jumlah:Int = 0
    val stopAt = 2500
    val pathArray:Array<City> = Array(14){ City(0, "",0.0, 0.0,0,0) }
    pathArray[0]= City(1 , "Curug Padjajaran/Bintang Padjajaran",-7.141948728625862, 107.42001699715476, 7,15)
    pathArray[1]= City(2, "Curug Awi Langit",-7.13786837250741, 107.4003322683194, 9, 15)
    pathArray[2]= City(3, "Curug Meong",-7.147259667366287, 107.43302599715466, 9, 15)
    pathArray[3]= City(4, "Curug Cigadong",-7.140309854149551, 107.40050826831946, 7, 15)
    pathArray[4]= City(5, "Latihan Pencak Silat",-7.134110954021378, 107.42146499715453, 14, 17)
    pathArray[5]= City(6, "Jeruk Dekopon dan Buah Tin Kebun Haji Iin",-7.1263228460283345, 107.4213933529785, 8, 10)
    pathArray[6]= City(7, "Wortel, bawang, kol",-7.131829661631447, 107.42214335297857, 6, 8)
    pathArray[7]= City(8, "Bawang daun, labusiam, wortel, strawberry",-7.134542142316982, 107.42175399715457, 6, 8)
    pathArray[8]= City(9, "Pinus Land",-7.162362963217781, 107.43179799715483, 11, 17)
    pathArray[9]= City(10, "Bird Watching",-7.158514509943944, 107.41803662414344, 11, 17)
    pathArray[10]= City (11, "Bunga Potong",-7.133991370886841, 107.42228635297856, 8, 10)
    pathArray[11]= City(12, "Karawitan dan Wayang Bodor",-7.127602305985058, 107.43041099715458, 10, 15)
    pathArray[12]= City(13, "Reog",-7.127168138379776, 107.43032926831937, 10, 15)
    pathArray[13]= City( 14, "Lampion, lukisan dari bambu",-7.130162723424981, 107.43236826831934, 14, 17)


    val currentTimestamp = System.currentTimeMillis()
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH.mm")
    val formatted = current.format(formatter)
    val time:Double = 7.1
    for (i in 0 until pathArray.size){
        if (pathArray[i].tutup!! > time && pathArray[i].buka!! < time){
            jumlah ++
        }
    }
    val pop:Population = Population(30,jumlah,1.0,1.0)
    if (GAUSE){
        pop?.FitnessOrder()
        while (numberOfGenerations != stopAt) {

//            Select / Crossover
            while (pop.Mate() == false)
//            Mutate
            for (i in 0 until pop.getNextGen().size) {
                pop.getNextGen()[i]!!.setPath(pop.Mutation(pop.getNextGen()[i]!!.getPath()))
            }

            //Setting the new Generation to current Generation
            pop.setPopulation(pop.getNextGen())
            pop.setDone(0)
            //Sorting the new population from Finess / Evaluating
            pop.FitnessOrder()
            //Incremente number of Generations
            numberOfGenerations++
        }

        var valor = 0.0
        for (i in 0 until pop.getPopulation().size) {
            valor += pop.getPopulation()[i]!!.getFitness()!!.toDouble()
            println("Value of Fitness: " + pop.getPopulation()[i]!!.getFitness() + "%")
        }
        println("")
        println("Total Fitness: $valor%")

        println("\n-----------------------------------------------")

        for (i in 0 until pop.getPopulation().size) {
            println("Path ID: " + i + " | Cost: Rp." + pop.getPopulation()[i]!!.getCost()?.roundToInt()!! * 1000 + "| Jarak: ${pop.getPopulation()[i]!!.getCost()} KM" + " | Fitness: " + pop.getPopulation()[i]!!.getFitness() + "%")
            print("Path is: ")
            for (j in 0 until pop.getPopulation()[i]!!.getPath().size) {
                print(pop.getPopulation()[i]!!.getPath()[j]!!.id.toString() + "(" + pop.getPopulation()[i]!!.getPath()[j]!!.getLat() + "," + pop.getPopulation()[i]!!.getPath()[j]!!.getLng() + ")  ")
            }
            println("\n -----------------------------------------------------")
        }




    }
}