(ns fmt-nm.main-test
  (:require [fmt-nm.main :refer :all]
            [clojure.test :refer :all]))

(deftest format-file-name_test
  (testing "format-file-name: "
    (testing "camel case"
      (is (= (format-file-name "camelCase")
             "camel-case")))
    (testing "title case"
      (is (= (format-file-name "TitleCase")
             "title-case")))
    (testing "kebab case"
      (is (= (format-file-name "kebab-case")
             "kebab-case")))
    (testing "lower case"
      (is (= (format-file-name "lower")
             "lower")))
    (testing "upper case"
      (is (= (format-file-name "UPPER")
             "upper")))
    (testing "lower case with spaces"
      (is (= (format-file-name "lower case")
             "lower-case")))
    (testing "upper case with spaces"
      (is (= (format-file-name "UPPER CASE")
             "upper-case")))
    (testing "capitalized kebab case"
      (is (= (format-file-name "Capitalized-Kebab-Case")
             "capitalized-kebab-case")))
    (testing "mixed cases"
      (is (= (format-file-name "WOah This-8 stringIs Crazy")
             "woah-this-8-string-is-crazy")))
    (testing "title case with spaces"
      (is (= (format-file-name "Title Case")
             "title-case")))
    (testing "file extensions should be ignored"
      (is (= (format-file-name "IgnoreThe.Exten sion")
             "ignore-the.Exten sion")))
    (testing "replace underscores"
      (is (= (format-file-name "replace_underscores")
             "replace-underscores")))
    (testing "drop commas"
      (is (= (format-file-name "Drop, The,Commas,dude")
             "drop-the-commas-dude")))))

(deftest seperate-file-name
  (testing "separate-file-name: "
    (testing "path from root"
      (is (let [res (separate-file-name "/path/from/root/foo.bar")]
            (and (= (second res)
                    "/path/from/root/")
                 (= (last res)
                    "foo.bar")))))
    (testing "relative path"
      (is (let [res (separate-file-name "relative/path/bar.baz")]
            (and (= (second res)
                    "relative/path/")
                 (= (last res)
                    "bar.baz")))))
    (testing "relative path with dots"
      (is (let [res (separate-file-name "../dotted/baz.qux")]
            (and (= (second res)
                    "../dotted/")
                 (= (last res)
                    "baz.qux")))))
    (testing "file with no extension"
      (is (let [res (separate-file-name "/somewhere/bar")]
            (and (= (second res)
                    "/somewhere/")
                 (= (last res)
                    "bar")))))))
