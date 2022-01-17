package danu.ga.ui.ui.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import danu.ga.algoritm.City
import danu.ga.algoritm.Population
import danu.ga.api.KordinatResponse
import danu.ga.api.NetworkModule
import danu.ga.databinding.FragmentGalleryBinding
import danu.ga.ui.ui.gallery.adapter.AdapterResponse
import danu.ga.ui.ui.gallery.adapter.NavigationWisataAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


class GalleryFragment : Fragment() {
    private var _binding: FragmentGalleryBinding? = null
    lateinit var mapView:MapView
    lateinit var googleMap: GoogleMap
    private val binding get() = _binding!!

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var settingClient: SettingsClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationSettingRequest: LocationSettingsRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var currentLocation: Location
    private var marker: Marker?= null
    private lateinit var kordinatUpdate:LatLng
    private var listMutasiKordinatPath:MutableList<LatLng> = mutableListOf()
    private var polyline: Polyline?= null

    private var cost:Int ?= null
    private var jarak:Double ?= null
    private var namaWisata:MutableList<AdapterResponse> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Jarak : ${jarak} dan Cost : Rp. ${cost}", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        //Location GPS
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
        settingClient = LocationServices.getSettingsClient(this.requireContext())
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireActivity(),"Enable Permission Location",Toast.LENGTH_LONG).show()
            binding.fab.visibility = View.GONE
            binding.listNavigation.visibility = View.GONE
        }
        else{
            binding.listNavigation.visibility = View.GONE
            binding.fab.visibility = View.VISIBLE
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location->
                if (location==null){
                    Toast.makeText(
                        this.requireContext(),
                        "Check You're Connection or Enable Your Location ",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else {
                    mapView = binding.mapView
                    mapView.onCreate(savedInstanceState)
                    mapView.onResume()
                    try {
                        MapsInitializer.initialize(requireContext())
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                    mapView.getMapAsync(OnMapReadyCallback{
                        googleMap = it
                        val myLocation = LatLng(location.latitude, location.longitude)
                        //Mutasi
                        var jumlah:Int = 0
                        var pathArray:Array<City> = Array(14){ City(0, "",0.0, 0.0,0,0) }
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
                        val time:Double = formatted.toDouble()
                        for (i in 0 until pathArray.size){
                            if (pathArray[i].tutup!! > time && pathArray[i].buka!! < time){
                                jumlah ++
                            }
                        }

                        //seleksi jumlah
                        listMutasiKordinatPath = ArrayList(jumlah+1)

                        if(jumlah != 0 ){
                            var numberOfGenerations:Int = 0
                            val stopAt:Int= 2500
                            val GAUSE = true
                            val pop: Population = Population(30,jumlah, 1.0, 1.0)
                            if (GAUSE){
                                pop.FitnessOrder()
                            }
                            while (numberOfGenerations != stopAt) {
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
                            for (j in 0 until pop.getPopulation()[pop.getPopulationSize()-1]!!.getPath().size){
                                listMutasiKordinatPath.add(
                                    LatLng(pop.getPopulation()[pop.getPopulationSize()-1]!!.getPath()[j]!!.getLat()!!.toDouble(),
                                        pop.getPopulation()[pop.getPopulationSize()-1]!!.getPath()[j]!!.getLng()!!.toDouble())
                                )
                            }
                            for (j in 0 until pop.getPopulation()[pop.getPopulationSize()-1]!!.getPath().size){
                                setNamaWisata(
                                    pop.getPopulation()[pop.getPopulationSize()-1]!!.getPath()[j]?.getNama()!!
                                )
                            }

                            //add data to database cost dan jarak
                            cost = pop.getPopulation()[pop.getPopulationSize() - 1]?.getCost()!!.roundToInt()*1000
                            val number:Double = pop.getPopulation()[pop.getPopulationSize()-1]!!.getCost()!!
                            val number3digits:Double = Math.round(number * 1000.0)/1000.0
                            val number2digits:Double = Math.round(number3digits * 100.0)/100.0
                            jarak = number2digits

                            // load api to map
                            NetworkModule.servicesKordinat().getData().enqueue(object :
                                Callback<KordinatResponse> {
                                override fun onResponse(
                                    call: Call<KordinatResponse>,
                                    response: Response<KordinatResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val data = response.body()?.body
                                        if (data?.size ?: 0 > 0) {
                                            val index: Int = data!!.size
                                            var angka = 0
                                            var myMarker1 = LatLng(-0.0, 0.0)


                                            //add data to map
                                            var latlngs: MutableList<LatLng> = mutableListOf()
                                            while (angka < index) {
                                                latlngs.add(
                                                    LatLng(data[angka]?.lat!!.toDouble(),
                                                        data[angka]?.lng!!.toDouble())
                                                )
                                                myMarker1 = LatLng(
                                                    data[angka]?.lat!!.toDouble(),
                                                    data[angka]?.lng!!.toDouble()
                                                )
                                                googleMap.addMarker(
                                                    MarkerOptions().position(myMarker1)
                                                        .title(data[angka]?.nama!!)
                                                )
                                                angka++
                                            }
                                            googleMap.addPolyline(
                                                PolylineOptions()
                                                    .add(myLocation)
                                                    .add(listMutasiKordinatPath[0])
                                                    .addAll(listMutasiKordinatPath))
                                        } else {
                                            print("Error")

                                        }
                                    }
                                }

                                override fun onFailure(call: Call<KordinatResponse>, t: Throwable) {
                                    print("Error")
                                }

                            })
                            //add Marker )
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10.0f))
                            googleMap.animateCamera(CameraUpdateFactory.zoomIn())
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f), 2000, null)
                            val cameraPosition = CameraPosition.Builder()
                                .target(myLocation) // Sets the center of the map to Mountain View
                                .zoom(17f) // Sets the zoom
                                .bearing(90f) // Sets the orientation of the camera to east
                                .tilt(30f) // Sets the tilt of the camera to 30 degrees
                                .build() // Creates a CameraPosition from the builder

                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                            locationRequest()
                            locationCallback()
                            buildSettingLocationRequet()
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,
                                Looper.getMainLooper())
                            showData(getNamaWisata())
                            binding.showListButton.setOnClickListener {
                                if (binding.listNavigation.visibility == View.VISIBLE){
                                    binding.listNavigation.visibility = View.GONE
                                    binding.showListButton.text = "SHOW"
                                }else{
                                    binding.listNavigation.visibility = View.VISIBLE
                                    binding.showListButton.text = "HIDE"
                                }
                            }
                        }else{
                            NetworkModule.servicesKordinat().getData().enqueue(object :
                                Callback<KordinatResponse> {
                                override fun onResponse(call: Call<KordinatResponse>, response: Response<KordinatResponse>) {
                                    if(response.isSuccessful){
                                        val data = response.body()?.body
                                        if (data?.size ?: 0 > 0) {
                                            val index: Int = data!!.size
                                            var angka = 0
                                            var myMarker1 = LatLng(-0.0, 0.0)
                                            //add data to map
                                            var latlngs: MutableList<LatLng> = mutableListOf()
                                            while (angka < index) {
                                                latlngs.add(
                                                    LatLng(data[angka]?.lat!!.toDouble(),
                                                        data[angka]?.lng!!.toDouble())
                                                )
                                                myMarker1 = LatLng(
                                                    data[angka]?.lat!!.toDouble(),
                                                    data[angka]?.lng!!.toDouble()
                                                )
                                                googleMap.addMarker(
                                                    MarkerOptions().position(myMarker1)
                                                        .title(data[angka]?.nama!!)
                                                )
                                                angka++
                                            }
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<KordinatResponse>, t: Throwable) {
                                    Toast.makeText(requireContext(),"Connection to API Failed",Toast.LENGTH_LONG).show()
                                }

                            })

                            //add Marker )
                            googleMap.addMarker(MarkerOptions().position(myLocation).title("My Location"))
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10.0f))
                            googleMap.animateCamera(CameraUpdateFactory.zoomIn())
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f), 2000, null)


                            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                            val cameraPosition = CameraPosition.Builder()
                                .target(myLocation) // Sets the center of the map to Mountain View
                                .zoom(17f) // Sets the zoom
                                .bearing(90f) // Sets the orientation of the camera to east
                                .tilt(30f) // Sets the tilt of the camera to 30 degrees
                                .build() // Creates a CameraPosition from the builder

                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                        }
                    })
                }
            }
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //function
    private fun locationRequest(){
        locationRequest = LocationRequest()
        locationRequest.setInterval(4000)
        locationRequest.setFastestInterval(2000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    }
    private fun locationCallback(){
        locationCallback = object :LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    currentLocation = locationResult.lastLocation
                    updateLocationUI()
                }
            }
        }
    }
    private fun buildSettingLocationRequet(){
        var builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        locationSettingRequest = builder.build()
    }
    private fun updateLocationUI(){
        if (marker==null && polyline==null){
            kordinatUpdate = LatLng(currentLocation.latitude,currentLocation.longitude)
            var a = MarkerOptions().position(kordinatUpdate)
            marker = googleMap.addMarker(a.title("My Location"))
            NetworkModule.servicesKordinat().getData().enqueue(object :
                Callback<KordinatResponse> {
                override fun onResponse(
                    call: Call<KordinatResponse>,
                    response: Response<KordinatResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.body
                        if (data?.size ?: 0 > 0) {
                            val index: Int = data!!.size
                            var angka = 0
                            var myMarker1 = LatLng(-0.0, 0.0)


                            //add data to map
                            var latlngs: MutableList<LatLng> = mutableListOf()
                            while (angka < index) {
                                latlngs.add(
                                    LatLng(data[angka]?.lat!!.toDouble(),
                                        data[angka]?.lng!!.toDouble())
                                )
                                myMarker1 = LatLng(
                                    data[angka]?.lat!!.toDouble(),
                                    data[angka]?.lng!!.toDouble()
                                )
                                googleMap.addMarker(
                                    MarkerOptions().position(myMarker1)
                                        .title(data[angka]?.nama!!)
                                )
                                angka++
                            }
                            listMutasiKordinatPath.add(0,kordinatUpdate)
                            polyline = googleMap.addPolyline(PolylineOptions().addAll(listMutasiKordinatPath))
                        } else {
                            print("Cau")

                        }
                    }
                }

                override fun onFailure(call: Call<KordinatResponse>, t: Throwable) {
                    print("Error")
                }

            })
        }else{
            kordinatUpdate = LatLng(currentLocation.latitude,currentLocation.longitude)
            marker!!.setPosition(kordinatUpdate)
            listMutasiKordinatPath.set(0,kordinatUpdate)
            polyline?.setPoints(listMutasiKordinatPath)

        }

    }
    //show data
    private fun showData(data: List<AdapterResponse>) {
        binding.listNavigation.adapter = NavigationWisataAdapter(data, this.requireContext())
    }
    //namaWisata
    private fun getNamaWisata():List<AdapterResponse>{
        return namaWisata
    }
    fun setNamaWisata(nama:String){
        namaWisata.add(AdapterResponse(nama))
    }
}