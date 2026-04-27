package com.littlelemon.application.menu.utils

import com.littlelemon.application.database.menu.MenuDao
import com.littlelemon.application.database.menu.models.CategoryEntity
import com.littlelemon.application.database.menu.models.DishCategoryCrossRef
import com.littlelemon.application.database.menu.models.DishEntity
import com.littlelemon.application.database.menu.models.DishWithCategories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMenuDao(
    seedDatabase: Boolean = true,
    private val throwError: Boolean = false,
) : MenuDao {

    private val dishEntities = mutableListOf<DishEntity>()
    val categoryEntities = mutableListOf<CategoryEntity>()
    private val dishCategoryCrossRefs = mutableListOf<DishCategoryCrossRef>()

    init {
        if (seedDatabase) {
            val dishCount = 20
            val categoryCount = 20

            categoryEntities.addAll(DishGenerator.generateCategoryEntities(categoryCount))
            repeat(dishCount) {
                val dish = DishGenerator.generateDishEntity().first
                dishEntities.add(dish)
                dishCategoryCrossRefs.addAll(
                    DishGenerator.generateDishCategoryCrossRef(
                        dish, categoryEntities
                    )
                )
            }
        }
    }

    override suspend fun insertCategories(categories: List<CategoryEntity>) {
        if (throwError) throw IllegalArgumentException()
        categoryEntities.removeIf { it in categories }
        categoryEntities.addAll(categories)
    }

    override suspend fun insertDishes(dishes: List<DishEntity>) {
        if (throwError) throw IllegalArgumentException()
        dishes.forEach { newDish ->
            dishEntities.removeIf { dish -> dish == newDish }
            dishEntities.add(newDish)
        }

    }

    override suspend fun insertDishCategoryCrossRefs(crossRefs: List<DishCategoryCrossRef>) {
        if (throwError) throw IllegalArgumentException()
        crossRefs.forEach { newCrossRef ->
            dishCategoryCrossRefs.removeIf { it.categoryId == newCrossRef.categoryId && it.dishId == newCrossRef.dishId }
            dishCategoryCrossRefs.add(newCrossRef)
        }
    }

    override suspend fun getDishCount(): Int = dishEntities.size

    private fun _getAllDishes(): List<DishWithCategories> {
        if (throwError) throw IllegalArgumentException()
        val returnedDishes = mutableListOf<DishWithCategories>()
        for (dish in dishEntities) {
            val categories = dishCategoryCrossRefs.filter { (dishId, _) -> dishId == dish.dishId }
                .mapNotNull { (_, categoryId) -> categoryEntities.find { category -> categoryId == category.categoryId } }
            returnedDishes.add(DishWithCategories(dish, categories))
        }
        return returnedDishes
    }

    override fun getAllDishes(): Flow<List<DishWithCategories>> = flow {
        if (throwError) throw IllegalArgumentException()
        val dishes = _getAllDishes()
        emit(dishes)
    }

    override fun getAllCategories(): Flow<List<CategoryEntity>> = flow {
        if (throwError) throw IllegalArgumentException()
        emit(categoryEntities)
    }

    override fun getDishesSortedByPopularity(): Flow<List<DishWithCategories>> = flow {
        if (throwError) throw IllegalArgumentException()
        val dishes = _getAllDishes().sortedByDescending { (dish, _) -> dish.popularityIndex }
        emit(dishes)
    }

    override fun getDishesSortedByName(ascending: Boolean): Flow<List<DishWithCategories>> = flow {
        if (throwError) throw IllegalArgumentException()
        val dishes = _getAllDishes()
        if (ascending) {
            emit(dishes.sortedBy { (dish, _) -> dish.title })
        } else {
            emit(dishes.sortedByDescending { (dish, _) -> dish.title })
        }
    }

    override fun getDishesSortedByPrice(ascending: Boolean): Flow<List<DishWithCategories>> = flow {
        if (throwError) throw IllegalArgumentException()
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
        if (throwError) throw IllegalArgumentException()
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
        if (throwError) throw IllegalArgumentException()
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
        if (throwError) throw IllegalArgumentException()
        val dishes = _getAllDishes()
        if (ascending) {
            emit(dishes.sortedBy { (dish, _) -> dish.nutritionInfo?.protein })
        } else {
            emit(dishes.sortedByDescending { (dish, _) -> dish.nutritionInfo?.protein })
        }
    }

    override suspend fun deleteAllDishes() {
        if (throwError) throw IllegalArgumentException()
        dishEntities.clear()
        categoryEntities.clear()
        dishCategoryCrossRefs.clear()
    }

}