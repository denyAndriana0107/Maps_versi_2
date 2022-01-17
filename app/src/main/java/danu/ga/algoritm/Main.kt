package danu.ga.maps.algoritm

import danu.ga.algoritm.FileReader
import danu.ga.algoritm.Path
import danu.ga.algoritm.Population
import kotlin.math.roundToInt


class Main {

}
fun main(){
//    var jumlah:Int = 0
//    var pathArray:Array<City> = Array(14){ City(0, 0.0, 0.0,0,0) }
////                pathArray[0] = City(1,-6.837117742309791,108.23474243750054,8,15)
////                pathArray[1] = City(2,-6.833511818974356,108.23374125613613,7,22)
////                pathArray[2] = City(3,-6.832790326613714,108.21439662980889,7,18)
////                pathArray[3] = City(4,-6.835272384200842,108.22775403108928,7,20)
//    pathArray[0]= City(1 , -7.141948728625862, 107.42001699715476, 7,17)
//    pathArray[1]= City(2, -7.13786837250741, 107.4003322683194, 7, 17)
//    pathArray[2]= City(3, -7.147259667366287, 107.43302599715466, 7, 17)
//    pathArray[3]= City(4, -7.140309854149551, 107.40050826831946, 7, 17)
//    pathArray[4]= City(5, -7.134110954021378, 107.42146499715453, 8, 21)
//    pathArray[5]= City(6, -7.1263228460283345, 107.4213933529785, 8, 21)
//    pathArray[6]= City(7, -7.131829661631447, 107.42214335297857, 8, 21)
//    pathArray[7]= City(8, -7.134542142316982, 107.42175399715457, 8, 21)
//    pathArray[8]= City(9, -7.162362963217781, 107.43179799715483, 9, 17)
//    pathArray[9]= City(10, -7.158514509943944, 107.41803662414344, 9, 21)
//    pathArray[10]= City (11, -7.133991370886841, 107.42228635297856, 9, 17)
//    pathArray[11]= City(12, -7.127602305985058, 107.43041099715458, 10, 21)
//    pathArray[12]= City(13, -7.127168138379776, 107.43032926831937, 10, 21)
//    pathArray[13]= City( 14, -7.130162723424981, 107.43236826831934, 10, 21)
//
//    val currentTimestamp = System.currentTimeMillis()
//    val current = LocalDateTime.now()
//    val formatter = DateTimeFormatter.ofPattern("HH.mm")
//    val formatted = current.format(formatter)
//    val time:Double = 12.0
//    for (i in 0 until pathArray.size){
//        if (pathArray[i].tutup!! > time && pathArray[i].buka!! < time){
//            jumlah ++
//        }
//    }
//    print("jumlah adalah $jumlah")


    var numberOfGenerations:Int = 4
    val stopAt:Int= 2500
    val GAUSE = true
    var numCities = 14
    var mutationRate :Double= 0.05
    var crossoverRate = 0.8
    var pop: Population ?= null
    var populationSize = 30
    val useFile = false
    val fileName = "Data.txt"
if (useFile == true){
    println("Tai")
}else{
    val file = FileReader(fileName)
    numCities = file.getNumCities()
    crossoverRate = file.getCrossoverRate()
    mutationRate = file.getMutationRate()
    populationSize = file.getCities()!!.size
    val p = Path(file.getNumCities())
    p.setPath(file.getCities()!!)
    pop = Population(populationSize, numCities, p, crossoverRate, mutationRate)
}

    if (GAUSE){
        pop?.FitnessOrder()
        for (i in 0 until pop?.getPopulation()!!.size) {
            println("Path ID: " + i + " | Cost: " + pop.getPopulation().get(i)!!.getCost() + " | Fitness: " + pop.getPopulation().get(i)!!.getFitness() + "%")
            print("Path is: ")
            for (j in 0 until pop.getPopulation().get(i)!!.getPath().size) {
                print(pop.getPopulation().get(i)!!.getPath().get(j)!!.getID().toString() + "(" + pop.getPopulation().get(i)!!.getPath().get(j)!!.getLat() + "," + pop.getPopulation().get(i)!!.getPath().get(j)!!.getLng() +
                        "," + pop.getPopulation().get(i)!!.getPath().get(j)!!.getBuka().toString() + "," + pop.getPopulation().get(i)!!.getPath().get(j)!!.getTutup() + ")  ")
            }
            println("\n -----------------------------------------------------")
        }

        while (numberOfGenerations !== stopAt) {

            //Select / Crossover
            while (pop.Mate() == false);
            //Mutate
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

          println("${pop.getPopulation()[pop.getPopulationSize() - 1]!!.getCost()?.roundToInt()!! * 1000}")



    }


}