package com.geode.binding;

class Test {
    public static void main(String[] args) {
        
        Property<String> nameProperty = new Property<String>();

        NotifyProperty<String> notifier = new NotifyProperty<String>("Arthur");

        notifier.bind(nameProperty, (src) -> {
            System.out.print(src + " <<< update");
            return src;
        });


        System.out.println(nameProperty);
    }
}
