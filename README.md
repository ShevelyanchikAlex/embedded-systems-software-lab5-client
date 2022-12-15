## Embedded Systems Software (lab-5)

### A protocol for two-way communication with a PC via a COM port.

### Implemented functionality

* Start/Stop receiving data from all sensors.
* Charts that receive data from the light sensor, potentiometer and temperature sensor.
* CheckBoxes by start/stop receiving data from specific sensors.
* ChoiceBox by changing delay time (1000/3000).
* Music player with ability change volume by potentiometer.
* Alert with sound by minimum temperature (+15 °C).

### Get Started

> Check which COM port is being used (in **CoolTerm** or **Device Manager**). After that you should change the com port number in the `service/config/SerialPortConfig`.
>
> Depending on the microcontroller and multi-purpose shield, it is recommended to ping microcontroller first using **CoolTerm**
or similar.
