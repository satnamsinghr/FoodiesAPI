package com.satnam.codesapi.user_service;

import com.satnam.codesapi.user_service.entity.FoodItem;
import com.satnam.codesapi.user_service.entity.Restaurant;
import com.satnam.codesapi.user_service.entity.Review;
import com.satnam.codesapi.user_service.entity.User;
import com.satnam.codesapi.user_service.repository.FoodItemRepository;
import com.satnam.codesapi.user_service.repository.RestaurantRepository;
import com.satnam.codesapi.user_service.repository.ReviewRepository;
import com.satnam.codesapi.user_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(basePackages = "com.satnam.codesapi.user_service.repository")
@EnableElasticsearchRepositories(basePackages = "com.satnam.codesapi.user_service.repository")
@EnableRedisRepositories(basePackages = "com.satnam.codesapi.user_service.repository")
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserRepository userRepository,
						  RestaurantRepository restaurantRepository,
						  FoodItemRepository foodItemRepository,
						  ReviewRepository reviewRepository,
						  PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.count() == 0) {
				userRepository.save(new User("Satnam", "satnam@example.com", passwordEncoder.encode("password123"), "9876543210", "USER"));
				userRepository.save(new User("John Doe", "john@example.com", passwordEncoder.encode("johnpass"), "9123456780", "USER"));
				userRepository.save(new User("Jane Smith", "jane@example.com", passwordEncoder.encode("janepass"), "9988776655", "USER"));
			}

			if (restaurantRepository.count() == 0) {
				Restaurant r1 = restaurantRepository.save(new Restaurant("Foodies Hub", "Hyderabad"));
				Restaurant r2 = restaurantRepository.save(new Restaurant("Tasty Bites", "Bangalore"));
				Restaurant r3 = restaurantRepository.save(new Restaurant("Café Delight", "Mumbai"));

				FoodItem f1 = foodItemRepository.save(new FoodItem("Pizza", "Cheesy delight", "https://via.placeholder.com/200", r1));
				FoodItem f2 = foodItemRepository.save(new FoodItem("Burger", "Juicy beef burger", "https://via.placeholder.com/200", r1));
				FoodItem f3 = foodItemRepository.save(new FoodItem("Pasta", "Italian pasta", "https://via.placeholder.com/200", r2));
				FoodItem f4 = foodItemRepository.save(new FoodItem("Sushi", "Fresh sushi rolls", "https://via.placeholder.com/200", r2));
				FoodItem f5 = foodItemRepository.save(new FoodItem("Salad", "Healthy green salad", "https://via.placeholder.com/200", r3));

				if (reviewRepository.count() == 0) {
					reviewRepository.save(new Review("Amazing taste!", 5, f1));
					reviewRepository.save(new Review("Too oily but good", 3, f2));
					reviewRepository.save(new Review("Authentic Italian flavor", 4, f3));
				}
			}
		};
	}
}
