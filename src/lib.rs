use std::io::Cursor;
use bsdiff::diff::diff;
use bsdiff::patch::patch;
use jni::objects::*;
use jni::sys::{jbyteArray, jlong};
use jni::JNIEnv;

#[no_mangle]
pub unsafe extern "C" fn Java_github_kasuminova_bsdiffnative_BsdiffNative_bsdiff(
    env: JNIEnv,
    _class: JClass,
    jold_array: jbyteArray,
    jnew_array: jbyteArray,
) -> jbyteArray {
    let mut cursor = Cursor::new(Vec::new());

    let old = env.convert_byte_array(jold_array).unwrap();
    let new = env.convert_byte_array(jnew_array).unwrap();

    match diff(&old, &new, &mut cursor) {
        Ok(_) => {}
        Err(err) => {
            env.throw_new("java/io/IOException", err.to_string()).unwrap();
        }
    }

    env.byte_array_from_slice(cursor.get_ref()).unwrap()
}

#[no_mangle]
pub unsafe extern "C" fn Java_github_kasuminova_bsdiffnative_BsdiffNative_bspatch(
    env: JNIEnv,
    _class: JClass,
    jold_array: jbyteArray,
    jpatch_bytes: jbyteArray,
    new_file_size: jlong,
) -> jbyteArray {
    let mut patched = vec![0; new_file_size as usize];

    let old = env.convert_byte_array(jold_array).unwrap();

    let mut patch_bytes = Cursor::new(env.convert_byte_array(jpatch_bytes).unwrap());

    match patch(&old, &mut patch_bytes, &mut patched) {
        Ok(_) => {}
        Err(err) => {
            env.throw_new("java/io/IOException", err.to_string()).unwrap();
        }
    }

    env.byte_array_from_slice(&patched).unwrap()
}

#[cfg(test)]
mod tests {
    use std::io::Cursor;
    use bsdiff::diff::diff;
    use bsdiff::patch::patch;

    #[test]
    fn it_works() {
        let old = vec![1, 2, 3, 4, 5];
        let new = vec![1, 2, 3, 4];
        let mut cursor = Cursor::new(Vec::new());

        diff(&old, &new, &mut cursor).unwrap();
        cursor.set_position(0);

        println!("patch package: {:?}", cursor);

        let mut patched = vec![0; new.len()];

        println!("starting patch...");
        patch(&old, &mut cursor, &mut patched).unwrap();

        println!("patch completed, result: {:?}", patched);

        assert_eq!(patched, new);
    }
}
