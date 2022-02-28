package ru.iu3.numlab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.iu3.numlab.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val quantity:Int = 50
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Формируем и отображаем ViewGroup
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Инициализируем список чисел, создаем адаптер для работы с набором RecyclerView, находим этот RecyclerView, привязываем к нему адаптер, задаем менеджер разметки для размещения View на экране, декорируем разделитель
        val numbers = numInit(quantity)
        val numAdapter = NumberAdapter(numbers)
        val recNumberView = findViewById<View>(R.id.number_recycler) as RecyclerView
        recNumberView.adapter = numAdapter
        recNumberView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        recNumberView.addItemDecoration(DividerItemDecoration(this,RecyclerView.VERTICAL))

        //Работа со спиннером: создаем список опций, отыскивем сам спиннер, создаем для него адаптер, прибавляем его к нему, содаем listener
        val options:ArrayList<String> = arrayListOf()
        options.add(getString(R.string.selector))
        options.add(getString(R.string.positive))
        options.add(getString(R.string.negative))
        options.add(getString(R.string.prime))
        val spinner:Spinner = findViewById(R.id.number_panel)
        val spinnerNumberAdapter:ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item,options)
        spinner.adapter=spinnerNumberAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            val myCopy = numbers.toMutableList()
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val newList:MutableList<NumberItem> = mutableListOf()
                when(p0?.getItemAtPosition(p2)){
                    getString(R.string.selector)->{
                        newList.addAll(myCopy)
                    }
                    getString(R.string.positive)->{
                        for(i in myCopy) if (i.sign=="positive") newList.add(i)
                    }
                    getString(R.string.negative)->{
                        for(i in myCopy) if (i.sign=="negative") newList.add(i)
                    }
                    getString(R.string.prime)->{
                        for(i in myCopy) if (i.complexity=="prime") newList.add(i)
                    }
                }
                numAdapter.swapNumbers(newList)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Another interface callback
            }

        }
    }

    private fun numInit(quantity:Int):MutableList<NumberItem>{
        val numberList = mutableListOf<NumberItem>()
        for (i in (-quantity/2)..(quantity/2)){
            val currentNumber = NumberItem(i,"","")
            currentNumber.setSign()
            currentNumber.setComplexity()
            numberList.add(currentNumber)
        }
        return numberList
    }
}