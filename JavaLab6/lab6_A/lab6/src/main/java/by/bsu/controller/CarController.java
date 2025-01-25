package by.bsu.controller;

import by.bsu.entities.Car;
import by.bsu.entities.Client;
import by.bsu.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/car")
public class CarController {

    @Autowired
    private CarRepository carRepository;


    @GetMapping("/info")
    public String carManagementPage(Model model) {
        List<Car> allCars = carRepository.findAll();
        model.addAttribute("allCars", allCars);
        return "/car/info";
    }

    @PostMapping("/info")
    public String addCar(
            @RequestParam("model") String model,
            @RequestParam("licensePlate") String licensePlate,
            @RequestParam("status") String status,
            Model pageModel) {
        try {
            Car car = new Car();
            car.setModel(model);
            car.setLicensePlate(licensePlate);
            car.setStatus(status);
            carRepository.save(car);
            return "redirect:/car/info";
        } catch (Exception e) {
            pageModel.addAttribute("errorMessage", "Error adding car: " + e.getMessage());
            return "/car/info";
        }
    }
    @GetMapping("/list")
    public String listCar(Model model) {
        List<Car> Cars = carRepository.findAll();
        model.addAttribute("cars", Cars);
        return "/car/list";
    }
}
