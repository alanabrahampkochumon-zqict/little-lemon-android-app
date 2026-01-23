import supabase from "../supabase.js";
// NOTE: Create Bucket in supabase for your project
/*
 * Uploads a file to a supabase bucket
 * @param bucketName: Name of the supabase bucket/folder to upload to
 * @param fileName: Filename that is saved in supabase
 * @param file: The file to be uploaded
 * @param upsert: Whether to update the existing file. Default "true"
 * @param contentType: Type of file. Default "image/png"
 */
export async function uploadImage(
    bucketName: string,
    fileName: string,
    file: Buffer | ArrayBuffer | Uint8Array,
    upsert: boolean = true,
    contentType: "image/png",
) {
    const { data, error } = await supabase.storage
        .from(bucketName)
        .upload(fileName, file, { upsert: upsert, contentType: contentType });

    if (error) console.error(`Error uploading image: ${error.message}`);
    else return data;
}
