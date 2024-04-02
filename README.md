# Auth Service
## Как запустить

1. Собираем jar архив с приложением:

    ``mvn clean package``

2. Пересоздаём ***образ*** и запускаем контейнер

    ``docker-compose up --build``

* предварительно должна быть создана docker сеть `hls-lab-network`
  для взаимодействия сервисов приложения:

  ``docker network create hls-lab-network``


* предварительно должна быть создана docker сеть `z-booking-internal-network`
  для взаимодействия компонентов внутри микросервиса (*БД*, *liquidbase*, *spring приложение*):

  ``docker network create z-booking-internal-network``