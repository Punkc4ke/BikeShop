package com.example.BikeShop.seeders;

import com.example.BikeShop.models.Malfunction;
import com.example.BikeShop.repositories.MalfunctionRepository;

import java.util.ArrayList;
import java.util.List;

public class MalfunctionSeeder {

    private static final List<Malfunction> malfunctionList = new ArrayList<>();

    private static void init() {
        malfunctionList.add(new Malfunction("Неисправность рамы"));
        malfunctionList.add(new Malfunction("Неисправность выноса руля"));
        malfunctionList.add(new Malfunction("Неисправность пера рамы"));
        malfunctionList.add(new Malfunction("Неисправность велосипедной вилки"));
        malfunctionList.add(new Malfunction("Неисправность регулятора жесткости"));
        malfunctionList.add(new Malfunction("Неисправность покрышек"));
        malfunctionList.add(new Malfunction("Неисправность обода"));
        malfunctionList.add(new Malfunction("Неисправность тормозного суппорта"));
        malfunctionList.add(new Malfunction("Неисправность тормозного диска"));
        malfunctionList.add(new Malfunction("Неисправность цепи"));
        malfunctionList.add(new Malfunction("Неисправность натяжного ролика"));
        malfunctionList.add(new Malfunction("Неисправность заднего переключателя"));
        malfunctionList.add(new Malfunction("Неисправность касеты"));
        malfunctionList.add(new Malfunction("Неисправность передних звезд"));
        malfunctionList.add(new Malfunction("Неисправность шатунов"));
        malfunctionList.add(new Malfunction("Неисправность педалей"));
        malfunctionList.add(new Malfunction("Неисправность эксцентрика"));
        malfunctionList.add(new Malfunction("Неисправность заднего амортизатора"));
        malfunctionList.add(new Malfunction("Неисправность эксцентрика подседельного"));
        malfunctionList.add(new Malfunction("Неисправность седла"));
        malfunctionList.add(new Malfunction("Неисправность подседельного штыря"));
        malfunctionList.add(new Malfunction("Неисправность заднего крыла"));
        malfunctionList.add(new Malfunction("Неисправность переднего крыла"));
        malfunctionList.add(new Malfunction("Неисправность троса"));
        malfunctionList.add(new Malfunction("Неисправность ручки"));
    }

    public static void seed(MalfunctionRepository malfunctionRepository) {
        init();
        for (Malfunction malfunction : malfunctionList)
            if (malfunctionRepository.findByName(malfunction.getName()) == null)
                malfunctionRepository.save(malfunction);
    }
}