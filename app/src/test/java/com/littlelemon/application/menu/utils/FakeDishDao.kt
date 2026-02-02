package com.littlelemon.application.menu.utils

import com.littlelemon.application.menu.data.local.dao.DishDao
import com.littlelemon.application.menu.data.local.models.CategoryEntity
import com.littlelemon.application.menu.data.local.models.DishCategoryCrossRef
import com.littlelemon.application.menu.data.local.models.DishEntity
import com.littlelemon.application.menu.data.local.models.DishWithCategories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDishDao(
    seedDatabase: Boolean = true
) : DishDao {

    private val dishes = mutableListOf<DishEntity>()
    private val categories = mutableListOf<CategoryEntity>()
    private val dishCategoryCrossRefs = mutableListOf<DishCategoryCrossRef>()

    init {
        if (seedDatabase) {
            val generator = MenuEntityGenerator()
            val dishCount = 20
            val categoryCount = 20

            categories.addAll(generator.generateCategoryEntities(categoryCount))
            repeat(dishCount) {
                val dish = generator.generateDishEntity()
                dishes.add(dish)
                dishCategoryCrossRefs.addAll(
                    generator.generateDishCategoryCrossRef(
                        dish,
                        categories
                    )
                )
            }
        }
    }

    override suspend fun insertCategories(category: CategoryEntity): Long {
        categories.removeIf { it.categoryId == category.categoryId }
        categories.add(category)
        return 1
    }

    override suspend fun insertDishes(dish: DishEntity): Long {
        dishes.removeIf { it.dishId == dish.dishId }
        dishes.add(dish)
        return 1

    }

    override suspend fun insertDishCategoryCrossRefs(crossRef: DishCategoryCrossRef) {
        dishCategoryCrossRefs.removeIf { it.categoryId == crossRef.categoryId && it.dishId == crossRef.dishId }
        dishCategoryCrossRefs.add(crossRef)
    }

    override suspend fun getDishCount(): Int = dishes.size

    private fun _getAllDishes(): List<DishWithCategories> {
        val returnedDishes = mutableListOf<DishWithCategories>()
        for (dish in dishes) {
            val categories =
                dishCategoryCrossRefs.filter { (dishId, _) -> dishId == dish.dishId }
                    .mapNotNull { (_, categoryId) -> categories.find { category -> categoryId == category.categoryId } }
            returnedDishes.add(DishWithCategories(dish, categories))
        }
        return returnedDishes
    }

    override fun getAllDishes(): Flow<List<DishWithCategories>> = flow {
        val dishes = _getAllDishes()
        emit(dishes)
    }

    override fun getDishesSortedByPopularity(): Flow<List<DishWithCategories>> = flow {
        val dishes = _getAllDishes().sortedByDescending { (dish, _) -> dish.popularityIndex }
        emit(dishes)
    }

    override fun getDishesSortedByName(ascending: Boolean): Flow<List<DishWithCategories>> = flow {
        val dishes = _getAllDishes()
        if (ascending) {
            emit(dishes.sortedBy { (dish, _) -> dish.title })
        } else {
            emit(dishes.sortedByDescending { (dish, _) -> dish.title })
        }
    }

    override fun getDishesSortedByPrice(ascending: Boolean): Flow<List<DishWithCategories>> = flow {
        val dishes = _getAllDishes()
        if (ascending) {
            emit(dishes.sortedBy { (dish, _) -> dish.price })
        } else {
            emit(dishes.sortedByDescending { (dish, _) -> dish.price })
        }
    }

    override fun getDishesSortedByAdded(
        ascending: Boolean
    ): Flow<List<DishWithCategories>> = flow {
        val dishes = _getAllDishes()
        if (ascending) {
            emit(dishes.sortedBy { (dish, _) -> dish.dateAdded })
        } else {
            emit(dishes.sortedByDescending { (dish, _) -> dish.dateAdded })
        }
    }

    override fun getDishesSortedByCalories(
        ascending: Boolean
    ): Flow<List<DishWithCategories>> = flow {
        val dishes = _getAllDishes()
        if (ascending) {
            emit(dishes.sortedBy { (dish, _) -> dish.nutritionInfo?.calories })
        } else {
            emit(dishes.sortedByDescending { (dish, _) -> dish.nutritionInfo?.calories })
        }
    }

    override fun getDishesSortedByProtein(
        ascending: Boolean
    ): Flow<List<DishWithCategories>> = flow {
        val dishes = _getAllDishes()
        if (ascending) {
            emit(dishes.sortedBy { (dish, _) -> dish.nutritionInfo?.protein })
        } else {
            emit(dishes.sortedByDescending { (dish, _) -> dish.nutritionInfo?.protein })
        }
    }

    override suspend fun deleteAllDishes() {
        dishes.clear()
        categories.clear()
        dishCategoryCrossRefs.clear()
    }
}