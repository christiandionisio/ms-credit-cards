FROM openjdk:8
ENV SPRING_PROFILES_ACTIVE prod
VOLUME /tmp
EXPOSE 9084
ADD ./target/ms-credit-card-0.0.1-SNAPSHOT.jar ms-credit-card.jar
ENTRYPOINT ["java","-jar","/ms-credit-card.jar"]