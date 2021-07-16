import core.*;
import core.Camera;

public class RoadController
{
    // Double passengerCarMaxWeight
    public static Double passengerCarMaxWeight = 3500.0; // kg
    // Integer passengerCarMaxHeight
    public static Integer passengerCarMaxHeight = 2000; // mm
    //Integer controllerMaxHeight
    public static Integer controllerMaxHeight = 3500; // mm
    //Integer passengerCarPrice
    public static Integer passengerCarPrice = 100; // RUB
    //Integer cargoCarPrice
    public static Integer cargoCarPrice = 250; // RUB
    //------вот тут не очень понятно, что это за переменная vehicleAdditionalPrice, это штраф на проводник сопроваждающий груз, или сам груз? ... или это транспортное средство?
    //------а если это проводник то может ли, он быть в легковом автомобиле? я испавил задачу согласно тому как понял
    //------ошибка с грузовым и легковым транспортом, явно была в том, что в цену штрафа перепутаны были переменные cargoCarPrice и passengerCarPrice,
    //------еще я не пойму зачем в исходнике сравнивается масса входящей машины с максимальным весом легковой (passengerCarMaxWeight), по идее если это продник  сопроваждающий груз(vehicle)
    //------то нужно сравнивать с массой пустой грузовой машины
    //------если она не проходит по высоте легковой(passengerCarMaxHeight), она уже не может быть легковой согласно логике, ну да ладно.....
    //Integer vehicleAdditionalPrice
    public static Integer vehicleAdditionalPrice = 200; // RUB
    //Integer maxOncomingSpeed
    public static Integer maxOncomingSpeed = 60; // km/h
    //Integer speedFineGrade
    public static Integer speedFineGrade = 20; // km/h
    //Integer finePerGrade
    public static Integer finePerGrade = 500; // RUB
    //Integer criminalSpeed
    public static Integer criminalSpeed = 160; // km/h

    public static void main(String[] args)
    {
        for(Integer i = 0; i < 10; i++)
        {
            //Car car переменная с типом класса
            Car car = Camera.getNextCar();
            System.out.println(car);
            System.out.println("Скорость: " + Camera.getCarSpeed(car) + " км/ч");

            /**
             * Проверка на наличие номера в списке номеров нарушителей
             */
            // Boolean policeCalled
            Boolean policeCalled = false;
            for(String criminalNumber : Police.getCriminalNumbers())
            {
                //String carNumber
                String carNumber = car.getNumber();
                if(carNumber.equals(criminalNumber)) {
                    Police.call("автомобиль нарушителя с номером " + carNumber);
                    blockWay("не двигайтесь с места! За вами уже выехали!");
                    break;
                }
            }
            if(Police.wasCalled()) {
                continue;
            }
            /**
             * Пропускаем автомобили спецтранспорта
             */
            if(car.isSpecial()) {
                openWay();
                continue;
            }

            /**
             * Проверяем высоту и массу автомобиля, вычисляем стоимость проезда
             */
            // Integer carHeight
            Integer carHeight = car.getHeight();
            // Integer price
            Integer price = 0;
            if(carHeight > controllerMaxHeight)
            {
                blockWay("высота вашего ТС превышает высоту пропускного пункта!");
                continue;
            }
            else if(carHeight > passengerCarMaxHeight)
            {
                //Double weight
                Double weight = WeightMeter.getWeight(car);
                //Грузовой автомобиль
                if(weight > passengerCarMaxWeight)
                {
                    price = cargoCarPrice;
                    if(car.hasVehicle()) {
                        price = price + vehicleAdditionalPrice;
                    }
                }
                else {
                    price = cargoCarPrice;
                }

            }
            //Легковой автомобиль
            else {
                price = passengerCarPrice;
                if(car.hasVehicle()) {
                    price = price + vehicleAdditionalPrice;
                }
            }

            /**
             * Проверка скорости подъезда и выставление штрафа
             */
            //Integer carSpeed
            Integer carSpeed = Camera.getCarSpeed(car);
            if(carSpeed > criminalSpeed)
            {
                Police.call("cкорость автомобиля - " + carSpeed + " км/ч, номер - " + car.getNumber());
                blockWay("вы значительно превысили скорость. Ожидайте полицию!");
                continue;
            }
            else if(carSpeed > maxOncomingSpeed)
            {
                // Integer overSpeed
                Integer overSpeed = carSpeed - maxOncomingSpeed;
                // Integer totalFine
                Integer totalFine = finePerGrade * (1 + overSpeed / speedFineGrade);
                System.out.println("Вы превысили скорость! Штраф: " + totalFine + " руб.");
                price = price + totalFine;
            }

            /**
             * Отображение суммы к оплате
             */
            System.out.println("Общая сумма к оплате: " + price + " руб.");
        }

    }

    /**
     * Открытие шлагбаума
     */
    public static void openWay()
    {
        System.out.println("Шлагбаум открывается... Счастливого пути!");
    }

    public static void blockWay(String reason)
    {
        System.out.println("Проезд невозможен: " + reason);
    }
}