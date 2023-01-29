package com.udacity.vehicles.service;

import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.util.List;
import org.springframework.stereotype.Service;

import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.client.maps.MapsClient;
import java.util.Optional;


/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository repository;
    private PriceClient priceClient;
    private MapsClient mapsClient;

    public CarService(CarRepository repository, PriceClient priceClient, MapsClient mapsClient) {
        /**
         *  Add the Maps and Pricing Web Clients you create
         *   in `VehiclesApiApplication` as arguments and set them here.
         */
        this.repository = repository;
        this.priceClient = priceClient;
        this.mapsClient = mapsClient;
    }

    /**
     * Gathers a list of all vehicles
     *
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return repository.findAll();
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     *
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        /**
         *  Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         *   Remove the below code as part of your implementation.
         */
        Car car = new Car();
        Optional<Car> optionalCar = repository.findById(id);
        if (optionalCar.isPresent()) {
            car = optionalCar.get();
        } else {
            throw new CarNotFoundException();
        }

        /**
         * Use the Pricing Web client you create in `VehiclesApiApplication`
         *   to get the price based on the `id` input'
         * Set the price of the car
         * Note: The car class file uses @transient, meaning you will need to call
         *   the pricing service each time to get the price.
         */
        String carPrice = priceClient.getPrice(id);
        car.setPrice(carPrice);

        /**
         * Use the Maps Web client you create in `VehiclesApiApplication`
         *   to get the address for the vehicle. You should access the location
         *   from the car object and feed it to the Maps service.
         * Set the location of the vehicle, including the address information
         * Note: The Location class file also uses @transient for the address,
         * meaning the Maps service needs to be called each time for the address.
         */
        Location carLocation = mapsClient.getAddress(car.getLocation());
        car.setLocation(carLocation);

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     *
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }

        return repository.save(car);
    }

    /**
     * Deletes a given car by ID
     *
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        /**
         * Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         */
        Optional<Car> optionalCar = repository.findById(id);

        /**
         * Delete the car from the repository.
         */

        if (optionalCar.isPresent()) {
            repository.delete(optionalCar.get());
        } else {
            throw new CarNotFoundException();
        }
    }
}
