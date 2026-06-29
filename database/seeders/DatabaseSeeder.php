<?php

namespace Database\Seeders;

use App\Models\User;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class DatabaseSeeder extends Seeder
{
    public function run(): void
    {
        $admin = User::updateOrCreate(
            ['email' => 'admin@seduhteh.com'],
            [
                'name'     => 'Admin Seduh Teh',
                'password' => Hash::make('admin123'),
            ]
        );
        $admin->forceFill(['role' => 'admin'])->save();

        $user = User::updateOrCreate(
            ['email' => 'user@seduhteh.com'],
            [
                'name'     => 'User Coba',
                'password' => Hash::make('user123'),
            ]
        );
        $user->forceFill(['role' => 'user'])->save();

        // produk teh + kategori
        $this->call(ProdukSeeder::class);
    }
}
