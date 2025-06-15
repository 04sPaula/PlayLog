package com.example.playlog

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager

class AllWatchedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_watched)

        val btnBackWard = findViewById<ImageView>(R.id.ic_backward)
        btnBackWard.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val recyclerViewGrid = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view_grid)

        val numeroDeColunas = 3

        val listaDeSeries: List<Serie> = listOf(
            Serie(
                "Sex and the City",
                "A colunista Carrie Bradshaw e suas três melhores amigas, Samantha, Charlotte e Miranda, exploram os altos e baixos do amor, sexo e amizade na agitada cidade de Nova York.",
                null,
                null
            ),
            Serie(
                "Friends",
                "Acompanha a vida de seis amigos que vivem em Manhattan, enfrentando os desafios do trabalho, dos relacionamentos e das situações cômicas do dia a dia no coração de Nova York.",
                null,
                null
            ),
            Serie(
                "Breaking Bad",
                "Um professor de química do ensino médio, diagnosticado com câncer terminal, decide entrar para o mundo do crime produzindo e vendendo metanfetamina para garantir o futuro financeiro de sua família.",
                null,
                null
            ),
            Serie(
                "The Office",
                "Esta comédia em estilo de pseudodocumentário retrata o cotidiano dos funcionários de um escritório na empresa de papel Dunder Mifflin, liderados pelo excêntrico e muitas vezes inadequado gerente Michael Scott.",
                null,
                null
            ),
            Serie(
                "Grey's Anatomy",
                "A série acompanha a jornada de Meredith Grey e um grupo de médicos no hospital Seattle Grace, mostrando suas vidas pessoais e profissionais enquanto lidam com casos médicos complexos e dramas intensos.",
                null,
                null
            )
        )

        val adapter = SeriesCarouselAdapter(listaDeSeries)
        recyclerViewGrid.adapter = adapter

        recyclerViewGrid.setLayoutManager(GridLayoutManager(this, numeroDeColunas))
    }
}