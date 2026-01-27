import supabase from "../supabase.js";

const { error, data } = await supabase
    .from("dish")
    .select(
        "id, title, description, category(category_name), nutrition_info(calories, protein, carbs, fats)",
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
