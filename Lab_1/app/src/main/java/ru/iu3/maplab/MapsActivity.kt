package ru.iu3.maplab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.iu3.maplab.databinding.MapContainerBinding

class MapsActivity : //точка входа в приложение
    AppCompatActivity(), //вызов конструктора базового класса для activity
    OnMapReadyCallback { //этот activity реализует интерфейс OnMapReadyCallback, подготавливающий карту Google к использованию

        private lateinit var mMap: GoogleMap
        /* GoogleMap представляет собой объект, с помощью которого выполняются следующие функции:
        -Подключение к сервису GoogleMaps
        -Скачивание карт
        -Отображение карт
        -Отображение элементов управления (перемещение и увеличение/уменьшение)
        -Реагирование на воздействие пользователя на элементы управления
        Именно через него осуществляется работа с картами (в т.ч. добавление маркеров)
         */
        private lateinit var binding: MapContainerBinding
        /*
        MapContainerBinding - автоматически сгенерированный класс для XML файла map_container.
        Он содержит ссылки на все элементы View (базового элемента пользовательского интерфейса), содержащиеся в
        родительском ViewGroup (объединении View - LinearLayout).
        Такая привязка ViewGroup к коду называется ViewBinding
         */
        private lateinit var myLocations: ArrayList<Locations>
        //Изменяемый список объектов dataclass, которые содержат информацию о метках на карте

        override fun onCreate(savedInstanceState: Bundle?) {
            /*
            В качестве параметра передаем Bundle - аналог Map для хранения отображений ключ-значение.
            В качестве ключа выступает string, а значения имеют примитивные или сериализуемые типы
             */
            super.onCreate(savedInstanceState)
            //вызов метода базового класса onCreate с передачей того же Bundle

            binding = MapContainerBinding.inflate(layoutInflater)
            /*
            Создаем объект класса MapContainerBinding, т.е. из ссылок на элементов формируем (надуваем) саму ViewGroup c помощью layoutInflater.
            layoutInflater - абстрактный класс, методы которого позволяют из XML файла сделать View
             */
            setContentView(binding.root)
            /*
            Передаем эту ViewGroup Activity, в результате чего она отображается на экране.
            binding.root - ссылка на эту ViewGroup
             */

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            /*
            supportFragmentManager - класс, который используется для взаимодействия с фрагментами (добавление, удаление, замена и т.д.)
            Фрагменты - части интерфейса, которые:
            -Представляют собой прямоугольные области, которые обрабатывают события польз.
            -Имеют свой жизненный цикл
            -Могут иметь свою разметку
            -Находятся внутри activity (single-activity подход)
            -Может переиспользоваться (в них может в разное время отображаться разный контент).
            Тут мы его используем для того, чтобы создать объект, ссылающийся на фрагмент, содержащий карту, чтобы с ним работать
             */
            mapFragment.getMapAsync(this)
            /*
            Асинхронно вызываем метод, получающий объект GoogleMap.
            После того, как метод завершит работу, он передает этот объект onMapReady
             */


            myLocations = locationInit()
            //Генерируем список объектов с информацией о городах на карте

            val places: ArrayList<String> = arrayListOf()
            for (i in 0 until myLocations.size) places.add(myLocations[i].name)
            //Создаем список наименований городов для передачи в спиннер
            val spinner: Spinner = findViewById<Spinner>(R.id.place_spinner)
            //Находим в ViewGroup нужный нам View со спиннером
            val placeAdapter: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item,places)
            /*
            Cоздаем объект класса ArrayAdapter - набор View, каждая из которых имеет вид android.R.layout.simple_spinner_item и
            соответствует одному из элементов списка places
             */
            spinner.adapter = placeAdapter
            //Закрепляем созданный объект за найденным спиннером

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                /*
                Создаем объект анонимного класса, который реализует интерфейс AdapterView.OnItemSelectedListener (обработчик события выбора города из выпадающего списка)
                AdapterView - View, у которого есть свои дети, которые определяются созданным ранее объектом ArrayAdapter
                 */
                override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                    val selected = myLocations.firstNotNullOfOrNull { item -> item.takeIf { item.name == parent.getItemAtPosition(pos)} }
                    //Получаем объект дата-класса (или null), соответствующий выбранному элементу списка
                    if (selected?.name == getString(R.string.DefaultCity)) return
                    //Если был выбран дефолтный вариант (не город), то тогда просто завершаем работу обработчика
                    else{
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selected?.location,5.0F))
                        /*
                        Перемещаем камеру так, чтобы в центре экрана оказалась метка с координатами выбранного города, увеличиваем уровнь масштабирования до 5
                        CameraUpdateFactory - класс, который содержит методы для работы с местоположением и ориентацией камеры (в данном случае для изменения местоположения и уровня масштабирования)
                         */
                        if (selected != null) Toast.makeText(applicationContext,getString(R.string.getPopulation)+selected.population.toString(),Toast.LENGTH_LONG).show()
                        /*
                        Выводим на экран всплывающее сообщение о численности населения в городе
                        Toast - маленькое сообщение-уведомление, которое появляется на экране на ограниченное время
                         */
                        else Toast.makeText(applicationContext,"Erroneous behaviour",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback
                }
            }
        }

        private fun locationInit(): ArrayList<Locations>{
            val places: ArrayList<Locations> = arrayListOf();
            places.add(Locations(getString(R.string.DefaultCity),LatLng(0.00,0.00),0))
            places.add(Locations(getString(R.string.Moscow), LatLng(55.755814,37.617635),12632409))
            places.add(Locations(getString(R.string.SaintPetersburgh),LatLng(59.939095,30.315868),5376672))
            places.add(Locations(getString(R.string.Sochi),LatLng(43.585525,39.723062),432322))
            places.add(Locations(getString(R.string.SouthSahalinsk),LatLng(46.9641,142.7285),200235))
            places.add(Locations(getString(R.string.Yaroslavl),LatLng(57.6261,39.8845),601403))
            return places;
        }

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
        override fun onMapReady(googleMap: GoogleMap) {
            mMap = googleMap
            for (i in 1 until myLocations.size){
                mMap.addMarker(MarkerOptions().position(myLocations.elementAt(i).location).title(myLocations.elementAt(i).name+". "+getString(R.string.getPopulation)+" "+myLocations.elementAt(i).population))
            }
            //Добавляем маркеры на карту (параметры метода определяют местоположение маркера и текст, всплывающий над маркером при нажатии на него (содержащий информацию о количестве жителей в городе)
        }
    }