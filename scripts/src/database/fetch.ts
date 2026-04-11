import supabase from "../supabase.js";

const { error, data } = await supabase
    .from("dish")
    .select(
        "id, title, description, price, discounted_price, stock, popularity_index, date_added, category(id, category_name), nutrition_info(calories, protein, carbs, fats), image",
    );

if (error) {
    console.error("Error occured:", error.message);
}
console.log(
    data?.map((dish) => {
        console.log(dish);
    }),
);
console.log(data);
